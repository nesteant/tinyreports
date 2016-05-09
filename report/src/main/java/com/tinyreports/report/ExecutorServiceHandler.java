package com.tinyreports.report;

import com.tinyreports.report.models.transfer.GroupingReport;
import com.tinyreports.report.models.transfer.SerializableBinding;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
public abstract class ExecutorServiceHandler {
    protected ExecutorService reportPool;
    protected ExecutorService rowPool;

    protected ExecutorServiceHandler(ExecutorService reportPool, ExecutorService rowPool) {
        this.reportPool = reportPool;
        this.rowPool = rowPool;
    }

    public ExecutorService getReportPool() {
        return reportPool;
    }

    public void setReportPool(ExecutorService reportPool) {
        this.reportPool = reportPool;
    }

    public ExecutorService getRowPool() {
        return rowPool;
    }

    public void setRowPool(ExecutorService rowPool) {
        this.rowPool = rowPool;
    }

    /**
     * WARNING: CALLED IN REPORT THREAD
     * Method is used to pass exception from Callable which is handled by reportPool
     *
     * @param e     exception which occurred in thread
     * @param fatal flag indicates if report module can operate normally after this type
     *              of exceptions
     */
    public abstract void reportReportException(Exception e, boolean fatal);

    /**
     * WARNING: CALLED IN ROW THREAD
     * Method is used to pass exception from Callable which is handled by rowPool
     *
     * @param e     exception which occurred in thread
     * @param fatal flag indicates if report module can operate normally after this type
     *              of exceptions
     */
    public abstract void reportRowException(Exception e, boolean fatal);

    /**
     * WARNING: CALLED IN MAIN
     * Method should return result only when all reports were processed
     */
    public abstract void awaitReportSubmissions() throws InterruptedException;

    /**
     * WARNING: CALLED IN MAIN
     * Should return Serializable Binding which was successfully returned from pool and delete it from queue.
     * This method is tightly coupled with {@link #hasMore()} and will be called iteratively until {@link #hasMore()}
     * returns true
     *
     * @return Binding which was successfully processed
     */
    public abstract SerializableBinding pollSerializableBinding() throws InterruptedException, ExecutionException;

    /**
     * WARNING: CALLED IN MAIN
     * identifies if pool still has bindings which were not polled by {@link #pollSerializableBinding()}
     *
     * @return true if there are still bindings which are not processed
     */
    public abstract boolean hasMore() throws InterruptedException;

    /**
     * WARNING: CALLED IN MAIN
     * Is called to submit tasks into ${@link #reportPool}
     *
     * @param callable task
     */
    public abstract void submitToReportPool(Callable<SerializableBinding> callable) throws InterruptedException;

    /**
     * WARNING: CALLED IN REPORT THREAD
     * Is called to execute in {@link #rowPool}. This method should take into account that tasks
     * are submitted by reportThreads and there is no possibility to call
     * {@link ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)}. Thus this
     * method should handle tasks using some cache in order to define if all tasks of current reportThread
     * are finished
     *
     * @param s task
     * @return uuid which is used to find state of {@param s}
     */
    public abstract String executeInRowPool(Runnable s);

    /**
     * WARNING: CALLED IN REPORT THREAD
     * Method is used to check if task submitted by {@link #executeInRowPool(Runnable)} successfully finished
     *
     * @param uuid uuid of submitted task
     * @return true if task is finished
     */
    public abstract boolean isRowTaskFinished(String uuid);

    /**
     * WARNING: CALLED IN MAIN
     * Method is called before exit from {@link com.tinyreports.report.generation.layout.GroupingReportBuilder}
     *
     * @param groupingReport current report
     */
    public abstract void onGenerationEnd(GroupingReport groupingReport) throws InterruptedException;

    public abstract void terminate() throws InterruptedException;
}
