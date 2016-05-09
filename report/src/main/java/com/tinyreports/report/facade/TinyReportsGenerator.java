package com.tinyreports.report.facade;

import com.tinyreports.common.DataProvider;
import com.tinyreports.report.DefaultExecutorServiceHandler;
import com.tinyreports.report.ExecutorServiceHandler;
import com.tinyreports.report.NonInterruptiveServiceHandler;
import com.tinyreports.report.factory.ReportBuilderFactory;
import com.tinyreports.report.generation.layout.GroupingReportBuilder;
import com.tinyreports.report.models.transfer.GroupingReport;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public final class TinyReportsGenerator {

    public static GroupingReport generate(DataProvider dataProvider, InputStream inputStream) throws Exception {
        ExecutorService reportPool = Executors.newFixedThreadPool(1);
        ExecutorService rowPool = Executors.newFixedThreadPool(1);
        return generate(dataProvider, inputStream, new DefaultExecutorServiceHandler(reportPool, rowPool));
    }

    public static GroupingReport generate(DataProvider dataProvider, InputStream inputStream, ExecutorServiceHandler executorServiceHandler) throws Exception {
        GroupingReportBuilder groupingReportBuilder = ReportBuilderFactory.getInstance().getGroupingReportBuilder(dataProvider, executorServiceHandler);
        return groupingReportBuilder.generate(inputStream);
    }

    public static GroupingReport generate(DataProvider dataProvider, InputStream inputStream, ExecutorService reportPool, ExecutorService rowPool) throws Exception {
        GroupingReportBuilder groupingReportBuilder = ReportBuilderFactory.getInstance().getGroupingReportBuilder(dataProvider, new NonInterruptiveServiceHandler(reportPool, rowPool));
        return groupingReportBuilder.generate(inputStream);
    }
}
