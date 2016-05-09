package com.tinyreports.report.generation.layout;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.report.ExecutorServiceHandler;
import com.tinyreports.report.models.transfer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.InputStream;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public final class GroupingReportBuilder {
    private static Logger LOGGER = LoggerFactory.getLogger(GroupingReportBuilder.class);
    private DataProvider dataProvider;
    private ExecutorServiceHandler executorServiceHandler;

    public GroupingReportBuilder(DataProvider dataProvider, ExecutorServiceHandler executorServiceHandler) {
        this.dataProvider = dataProvider;
        this.executorServiceHandler = executorServiceHandler;
    }

    public GroupingReport generate(InputStream is) throws Exception {
        GroupingReport groupingReport = new GroupingReport();
        Document layout = DomOperations.parseBHtml(new InputSource(is));
        Element root = layout.getDocumentElement();
        NodeVisitor.visitChildren(preppareBuildInfo(layout), root);
        executorServiceHandler.awaitReportSubmissions();
        while (executorServiceHandler.hasMore()) {
            SerializableBinding binding = executorServiceHandler.pollSerializableBinding();
            if (binding == null) {
                continue;
            }
            if (binding instanceof ReportBinding) {
                groupingReport.getReportBindings().add((ReportBinding) binding);
            } else if (binding instanceof InjectBinding) {
                groupingReport.getInjections().add((InjectBinding) binding);
            } else {
                groupingReport.getChartBindings().add((ChartBinding) binding);
            }
        }
        groupingReport.setLayout(DomOperations.toString(layout));
        executorServiceHandler.onGenerationEnd(groupingReport);
        return groupingReport;
    }

    private BuildInfo preppareBuildInfo(Document layout) {
        BuildInfo buildInfo = new BuildInfo();
        buildInfo.setLayout(layout);
        buildInfo.setGlobalContext(new StandardEvaluationContext());
        buildInfo.setDataProvider(dataProvider);
        buildInfo.setExecutorServiceHandler(executorServiceHandler);
        return buildInfo;
    }
}
