package com.tinyreports.report.factory;

import com.tinyreports.common.DataProvider;
import com.tinyreports.report.ExecutorServiceHandler;
import com.tinyreports.report.generation.layout.GroupingReportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ReportBuilderFactory {
    private static Logger LOGGER = LoggerFactory.getLogger(ReportBuilderFactory.class);
    private static ReportBuilderFactory reportBuilderFactory = new ReportBuilderFactory();
    private static DataProvider DATA_PROVIDER;

    public static ReportBuilderFactory getInstance() {
        return reportBuilderFactory;
    }

    public GroupingReportBuilder getGroupingReportBuilder(DataProvider dataProvider, ExecutorServiceHandler executorServiceHandler) {
        if (DATA_PROVIDER == null) {
            DATA_PROVIDER = new DataProvider();
        }
        return new GroupingReportBuilder(dataProvider, executorServiceHandler);
    }
}
