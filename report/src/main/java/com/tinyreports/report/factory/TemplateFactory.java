package com.tinyreports.report.factory;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.UniqueTemplate;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.report.generation.template.ChartTemplateBuilderImpl;
import com.tinyreports.report.generation.template.ReportTemplateBuilderImpl;
import com.tinyreports.report.models.templates.ChartTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class TemplateFactory {
    public static UniqueTemplate newInstance(InputStream inputStream) throws TinyReportException {
        Document document = DomOperations.parseXml(new InputSource(inputStream));
        NodeList reportList = document.getElementsByTagNameNS(TemplateVariables.NS, TemplateVariables.REPORT_TAG);
        NodeList chartList = document.getElementsByTagNameNS(TemplateVariables.NS, TemplateVariables.CHART_TAG);
        if (reportList.getLength() == 0 && chartList.getLength() == 0) {
            throw new TinyReportTemplateException("Template should have at least one %s or %s tag", TemplateVariables.REPORT_TAG, TemplateVariables.CHART_TAG);
        } else if (reportList.getLength() > 0) {
            Element templateElement = (Element) reportList.item(0);
            return new ReportTemplateBuilderImpl().convertToTemplate(templateElement);
        } else {
            Element templateElement = (Element) chartList.item(0);
            return new ChartTemplateBuilderImpl().convertToTemplate(templateElement);
        }
    }

    public static void validate(UniqueTemplate ut) {
        if (ut instanceof ReportTemplate) {
            new ReportTemplateBuilderImpl().validate((ReportTemplate) ut);
        } else if (ut instanceof ChartTemplate) {
            new ChartTemplateBuilderImpl().validate((ChartTemplate) ut);
        }
    }
}
