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
