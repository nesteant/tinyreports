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

package com.tinyreports.report.generation.layout;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.report.ExecutorServiceHandler;
import com.tinyreports.report.models.transfer.ChartBinding;
import com.tinyreports.report.models.transfer.GroupingReport;
import com.tinyreports.report.models.transfer.InjectBinding;
import com.tinyreports.report.models.transfer.ReportBinding;
import com.tinyreports.report.models.transfer.SerializableBinding;
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
			} else if (binding instanceof InjectBinding){
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
