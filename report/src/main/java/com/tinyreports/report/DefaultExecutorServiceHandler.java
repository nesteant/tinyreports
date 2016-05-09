/*
 * Tinyreports
 * Copyright (c) 2013. Anton Nesterenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tinyreports.report;

import com.tinyreports.report.models.transfer.GroupingReport;
import com.tinyreports.report.models.transfer.SerializableBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
