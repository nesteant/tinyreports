package com.tinyreports.report.generation.layout;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import com.tinyreports.common.utils.ArrayUtils;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.factory.TemplateFactory;
import com.tinyreports.report.generation.report.ReportBuilder;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.models.transfer.ReportBinding;
import com.tinyreports.report.models.transfer.SerializableBinding;
import com.tinyreports.report.models.transfer.XmlReport;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class ReportNodeVisitor extends LayoutVisitor implements Visitable {
    protected ReportNodeVisitor(BuildInfo buildInfo) {
        super(buildInfo);
    }

    @Override
    public void visit(Node n) throws TinyReportException, InterruptedException {
        ReportTemplate reportTemplate = new XmlDeserializer<ReportTemplate>(ReportTemplate.class).deserialize(n);
        Node clone = n.cloneNode(false);
        TemplateFactory.validate(reportTemplate);
        String printWhenExpression = reportTemplate.getPrintWhenExpression();
        Boolean shouldPrint = true;
        if (StringUtils.isNotEmpty(printWhenExpression)) {
            shouldPrint = evaluateLayoutExpression(globalContext, dataProvider, printWhenExpression, Boolean.class);
        }
        if (shouldPrint) {
            String uuid = UUID.randomUUID().toString();
            ((Element) clone).setAttribute("uuid", uuid);
            scheduleReportGeneration(uuid, reportTemplate);
            Node parent = n.getParentNode();
            parent.replaceChild(clone, n);
        } else {
            Node parent = n.getParentNode();
            parent.removeChild(n);
        }
    }

    private void scheduleReportGeneration(String uuid, ReportTemplate reportTemplate) throws TinyReportTemplateException, InterruptedException {
        String iteratorId = reportTemplate.getIteratorId();
        Object currentObject;
        if (StringUtils.isNotEmpty(iteratorId)) {
            if (iteratorId.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
                ContextUtils.loadToContext(globalContext, null, dataProvider, iteratorId);
                currentObject = ContextUtils.evaluateNonSafeExpression(globalContext, iteratorId);
            } else {
                currentObject = dataProvider.getDataObjectByKey(iteratorId);
            }
            InnerReportWorker reportWorker = new InnerReportWorker(uuid, reportTemplate, currentObject);
            executorServiceHandler.submitToReportPool(reportWorker);
        } else {
            throw new IllegalStateException(String.format("Iterator cannot be null. ReportId: %s", reportTemplate.getId()));
        }
    }

    class InnerReportWorker implements Callable<SerializableBinding> {
        private String reportUuid;
        private ReportTemplate reportTemplate;
        private Object currentObject;

        InnerReportWorker(String reportUuid, ReportTemplate reportTemplate, Object currentObject) {
            this.reportUuid = reportUuid;
            this.reportTemplate = reportTemplate;
            this.currentObject = currentObject;
        }

        @Override
        public SerializableBinding call() throws Exception {
            ReportBuilder reportBuilder = new ReportBuilder(dataProvider, reportTemplate, new HashMap<String, Object>());
            try {
                List<Object> objs = ArrayUtils.linearalizeArrayOfObjects(currentObject);
                generateByRows(reportBuilder, objs);
            } catch (Exception e) {
                //TODO exception handling
                executorServiceHandler.reportReportException(e, true);
                throw e;
            }
            XmlReport xmlReport = reportBuilder.getXmlReport();
            return new ReportBinding(reportUuid, xmlReport);
        }

        private void generateByRows(final ReportBuilder reportBuilder, Collection iterators) throws InterruptedException {
            Queue<String> uuidToWait = new ConcurrentLinkedQueue<String>();
            for (final Object iteratorObject : iterators) {
                String uuid = executorServiceHandler.executeInRowPool(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            reportBuilder.generateXmlRow(iteratorObject);
                        } catch (Exception e) {
                            LOGGER.error("Exception in thread", e);
                            executorServiceHandler.reportRowException(e, true);
                        }
                    }
                });
                uuidToWait.add(uuid);
            }
            //TODO possible we need to add infinity check
            while (uuidToWait.size() > 0) {
                for (String uuid : uuidToWait) {
                    if (executorServiceHandler.isRowTaskFinished(uuid)) {
                        uuidToWait.remove(uuid);
                    }
                }
            }
            reportBuilder.finalizeGeneration(iterators);
        }
    }
}
