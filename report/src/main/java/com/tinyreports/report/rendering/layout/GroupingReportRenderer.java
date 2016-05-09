package com.tinyreports.report.rendering.layout;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.facade.XmlReportRenderer;
import com.tinyreports.report.models.layout.DetachedSorting;
import com.tinyreports.report.models.layout.SortingElement;
import com.tinyreports.report.models.transfer.*;
import com.tinyreports.report.models.transfer.csv.CsvReport;
import com.tinyreports.report.models.transfer.csv.GroupingCsvReport;
import com.tinyreports.report.rendering.NaturalOrderComparator;
import com.tinyreports.report.rendering.report.ReportRenderer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Matcher;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
public class GroupingReportRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupingReportRenderer.class);
    private GroupingReport groupingReport;

    public GroupingReportRenderer(GroupingReport groupingReport) {
        this.groupingReport = groupingReport;
    }

    public GroupingCsvReport csvRender() throws TinyReportException {
        GroupingCsvReport groupingCsvReport = new GroupingCsvReport();
        List<CsvReport> csvReportList = new ArrayList<CsvReport>();
        List<ReportBinding> reportBindings = groupingReport.getReportBindings();
        for (ReportBinding reportBinding : reportBindings) {
            CsvReport csvReport = XmlReportRenderer.csvRender(reportBinding.getXmlReport());
            csvReportList.add(csvReport);
        }
        groupingCsvReport.setCsvReportList(csvReportList);
        return groupingCsvReport;
    }

    public String render() throws TinyReportException {
        String layout = groupingReport.getLayout();
        Document layoutDocument = DomOperations.toHtmlDocument(layout);
        Comparator<Element> nodeComparator = new NodeComparator();
        NodeList expandedGroupings = layoutDocument.getElementsByTagNameNS(TemplateVariables.NS, TemplateVariables.GROUPING_WRAPPER_TAG);
        for (int i = 0; i < expandedGroupings.getLength(); i++) {
            Element expandedGrouping = (Element) expandedGroupings.item(i);
            NodeList groups = expandedGrouping.getChildNodes();
            List<Element> sortCollection = new ArrayList<Element>();
            for (int j = 0; j < groups.getLength(); j++) {
                sortCollection.add((Element) groups.item(j));
            }
            Collections.sort(sortCollection, nodeComparator);
            for (Element group : sortCollection) {
                group.removeChild(findExactSortingTag(group));
                expandedGrouping.appendChild(group);
            }
        }
        layout = DomOperations.toString(layoutDocument);

        StringBuffer rb = new StringBuffer();
        Matcher matcher = TemplateVariables.SERIALIZABLE_REPLACEMENT.matcher(layout);
        while (matcher.find()) {
            String all = matcher.group();
            String uuid = matcher.group(2);
            SerializableBinding binding = groupingReport.getBinding(uuid);
            String replacement;
            //TODO introduce generics
            if (binding instanceof ReportBinding) {
                replacement = workOnReportBinding((ReportBinding) binding);
            } else if (binding instanceof ChartBinding) {
                replacement = workOnChartBinding((ChartBinding) binding);
            } else if (binding instanceof InjectBinding) {
                String value = ((InjectBinding) binding).getValue();
                replacement = "<script type=\"text/javascript\">" + value + "</script>\n\r";
            } else {
                LOGGER.warn("Unknown Binding: {}", uuid);
                replacement = StringUtils.EMPTY;
            }
            if (replacement == null) {
                replacement = StringUtils.EMPTY;
            }
            replacement = Matcher.quoteReplacement(replacement);
            matcher.appendReplacement(rb, replacement);
        }
        matcher.appendTail(rb);
        String notCleanedString = rb.toString();
        //TODO create right way of doctype declaration
        return "<!DOCTYPE html>".concat(cleanup(notCleanedString));
    }

    private String workOnReportBinding(ReportBinding binding) throws TinyReportException {
        XmlReport xmlReport = binding.getXmlReport();
        boolean isBlank = xmlReport.getBlank();
        if (!isBlank) {
            return new ReportRenderer(xmlReport).render(String.class);
        } else {
            if (StringUtils.isNotEmpty(xmlReport.getBlankText())) {
                return new ReportRenderer(xmlReport).render(String.class);
            }
            return StringUtils.EMPTY;
        }
    }

    private String workOnChartBinding(ChartBinding binding) {
        String scriptString = binding.getScript();
        String containerString = binding.getContainer();
        String scriptTagString = "<script type=\"text/javascript\">" + scriptString + "</script>\n\r";
        return scriptTagString + containerString;
    }

    private String cleanup(String layout) {
        return layout
                .replaceAll("tiny:grouping", "div")
                .replaceAll("tiny:group", "div")
                .replaceAll("tiny:complex", "div")
                .replaceAll("xmlns(:\\w)?=\"[^\"]*\"", "")
                .replaceAll("uuid=\"[^\"]*\"\", \"\"", "")
                .replaceAll("(?i)printWhen=\"[^\"]*\"\", \"\"", "");
    }

    class NodeComparator implements Comparator<Element> {
        @Override
        public int compare(Element o1, Element o2) {
            Element sortingElement1 = findExactSortingTag(o1);
            Element sortingElement2 = findExactSortingTag(o2);
            try {
                DetachedSorting sorting1 = new XmlDeserializer<DetachedSorting>(DetachedSorting.class).deserialize(sortingElement1);
                DetachedSorting sorting2 = new XmlDeserializer<DetachedSorting>(DetachedSorting.class).deserialize(sortingElement2);
                List<SortingElement> sorts1 = sorting1.getSorting();
                List<SortingElement> sorts2 = sorting2.getSorting();
                if (sorts1.size() != sorts2.size()) {
                    LOGGER.error("Uncomparable nodes. Sorting avoided");
                    return 0;
                }
                for (SortingElement sort1 : sorts1) {
                    SortingElement sort2 = getByField(sorts2, sort1.getField());
                    if (sort2 == null) {
                        LOGGER.error("Sort fields are different. Avoid sorting");
                        return 0;
                    }
                    int comp = compareWithOrder(sort1.getValue(), sort2.getValue(), sort1.getOrder());
                    if (comp != 0) {
                        return comp;
                    }
                }
            } catch (TinyReportException e) {
                LOGGER.error("Failed to deserialize sorting node. Sorting is not applied", e);
                return 0;
            }
            return 0;
        }

        private int compareWithOrder(String string1, String string2, String order) {
            if (order.equalsIgnoreCase("asc")) {
                return compareTo(string1, string2);
            } else {
                return -compareTo(string1, string2);
            }
        }

        private int compareTo(String string1, String string2) {
            if (string1 == null) {
                string1 = StringUtils.EMPTY;
            }
            if (string2 == null) {
                string2 = StringUtils.EMPTY;
            }
            return NaturalOrderComparator.compare(string1, string2);
        }

        public SortingElement getByField(Collection<SortingElement> sorting, String field) {
            for (SortingElement sortingElement : sorting) {
                if (sortingElement.getField().equals(field)) {
                    return sortingElement;
                }
            }
            return null;
        }
    }

    private Element findExactSortingTag(Element el) {
        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node item = children.item(i);
            if (item.getNodeName().endsWith(TemplateVariables.SORTING_WRAPPER_TAG)) {
                return (Element) item;
            }
        }
        return null;
    }
}
