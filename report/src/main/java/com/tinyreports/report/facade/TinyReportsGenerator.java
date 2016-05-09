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
