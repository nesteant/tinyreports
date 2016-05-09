package com.tinyreports.report;

import com.tinyreports.report.models.transfer.GroupingReport;
import com.tinyreports.report.models.transfer.SerializableBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
public class DefaultExecutorServiceHandler extends ExecutorServiceHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExecutorServiceHandler.class);
    protected Map<String, Future> taskCache = new ConcurrentHashMap<String, Future>();
    protected List<Future<SerializableBinding>> futureHolder = Collections.synchronizedList(new ArrayList<Future<SerializableBinding>>());
    protected boolean failed = false;

    public DefaultExecutorServiceHandler(ExecutorService reportPool, ExecutorService rowPool) {
        super(reportPool, rowPool);
    }

    @Override
    public void reportReportException(Exception e, boolean fatal) {
        LOGGER.error("Exception", e);
        failed = true;
    }

    @Override
    public void reportRowException(Exception e, boolean fatal) {
        LOGGER.error("Exception", e);
        failed = true;
    }

    @Override
    public void awaitReportSubmissions() throws InterruptedException {
        reportPool.shutdown();
        reportPool.awaitTermination(1000, TimeUnit.HOURS);
    }

    @Override
    public SerializableBinding pollSerializableBinding() throws InterruptedException, ExecutionException {
        if (failed) {
            terminate();
        }
        if (futureHolder.size() > 0) {
            for (Future<SerializableBinding> future : futureHolder) {
                if (future.isDone()) {
                    SerializableBinding binding = future.get();
                    futureHolder.remove(future);
                    return binding;
                }
            }
        } else {
            throw new IndexOutOfBoundsException("Futures are not present anymore");
        }
        return null;
    }

    @Override
    public boolean hasMore() throws InterruptedException {
        if (failed) {
            terminate();
        }
        return futureHolder.size() > 0;
    }

    @Override
    public void submitToReportPool(Callable<SerializableBinding> callable) throws InterruptedException {
        if (failed) {
            terminate();
        }
        Future<SerializableBinding> future = reportPool.submit(callable);
        futureHolder.add(future);
    }

    @Override
    public String executeInRowPool(Runnable s) {
        String uuid = UUID.randomUUID().toString();
        Future<?> future = rowPool.submit(s);
        taskCache.put(uuid, future);
        return uuid;
    }

    @Override
    public boolean isRowTaskFinished(String uuid) {
        return taskCache.get(uuid).isDone();
    }

    @Override
    public void onGenerationEnd(GroupingReport groupingReport) throws InterruptedException {
        rowPool.shutdown();
        reportPool.shutdown();
        if (failed) {
            terminate();
        }
    }

    @Override
    public void terminate() throws InterruptedException {
        rowPool.shutdown();
        reportPool.shutdown();
        rowPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        reportPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        rowPool.shutdownNow();
        reportPool.shutdownNow();
        throw new InterruptedException("Termination called");
    }
}
