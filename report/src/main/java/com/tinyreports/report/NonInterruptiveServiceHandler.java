package com.tinyreports.report;

import com.tinyreports.report.models.transfer.GroupingReport;

import java.util.concurrent.ExecutorService;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
public class NonInterruptiveServiceHandler extends DefaultExecutorServiceHandler {
    public NonInterruptiveServiceHandler(ExecutorService reportPool, ExecutorService rowPool) {
        super(reportPool, rowPool);
    }

    @Override
    public void onGenerationEnd(GroupingReport groupingReport) throws InterruptedException {
        if (failed) {
            terminate();
        }
    }

    @Override
    public void terminate() throws InterruptedException {
        throw new InterruptedException("Termination called");
    }
}
