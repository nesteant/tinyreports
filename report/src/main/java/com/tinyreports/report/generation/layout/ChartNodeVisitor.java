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

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import com.tinyreports.common.utils.Pair;
import com.tinyreports.report.generation.report.ChartBuilder;
import com.tinyreports.report.models.templates.ChartTemplate;
import com.tinyreports.report.models.transfer.ChartBinding;
import com.tinyreports.report.models.transfer.SerializableBinding;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class ChartNodeVisitor extends LayoutVisitor implements Visitable {

	protected ChartNodeVisitor(BuildInfo buildInfo) {
		super(buildInfo);
	}

	@Override
	public void visit(Node n) throws TinyReportException, InterruptedException {
		Element chartElement = (Element) n;
		Element clone = (Element) chartElement.cloneNode(false);

		String chartUuid = UUID.randomUUID().toString();
		clone.setAttribute(UUID_ATTR, chartUuid);
		String chartId = chartElement.getAttribute(CHART_ID_ATTR);
		String printWhenExpression = chartElement.getAttribute(PRINT_WHEN_REPORT_ATTR);

		Boolean shouldPrint = null;
		if (!StringUtils.isEmpty(printWhenExpression)) {
			shouldPrint = evaluateLayoutExpression(globalContext, dataProvider, printWhenExpression, Boolean.class);
		}

		List<Pair<String, String>> copyValuePairs = new ArrayList<Pair<String, String>>();
		List<Pair<String, String>> addValuePairs = new ArrayList<Pair<String, String>>();

		NodeList copyValueNodes = chartElement.getElementsByTagNameNS(TemplateVariables.NS, COPY_VALUE);
		NodeList addValueNodes = chartElement.getElementsByTagNameNS(TemplateVariables.NS, ADD_VALUE);

		if (addValueNodes != null) {
			for (int i = 0; i < addValueNodes.getLength(); i++) {
				Pair<String, String> valuePair = processValueNode((Element) addValueNodes.item(i));
				addValuePairs.add(valuePair);
			}
		}
		if (copyValueNodes != null) {
			for (int i = 0; i < copyValueNodes.getLength(); i++) {
				Pair<String, String> valuePair = processValueNode((Element) copyValueNodes.item(i));
				copyValuePairs.add(valuePair);
			}
		}

		if (shouldPrint == null || shouldPrint) {
			scheduleChartGeneration(chartId, chartUuid, copyValuePairs, addValuePairs);

			Node parent = n.getParentNode();
			parent.replaceChild(clone, n);
		} else {
			Node parent = n.getParentNode();
			parent.removeChild(n);
		}
	}

	private Pair<String, String> processValueNode(Element element) {
		String valueId = element.getAttribute(ID);
		String value = element.getTextContent();
		return new Pair<String, String>(valueId, value);
	}

	private void scheduleChartGeneration(String chartId, String chartUuid, List<Pair<String, String>> copyValuePairs, List<Pair<String, String>> addValuePairs) throws TinyReportTemplateException, InterruptedException {
		ChartTemplate template = (ChartTemplate) dataProvider.getDataObjectByKey(chartId);
		if (template == null) {
			throw new TinyReportTemplateException("Template %s not found in dataProvider", chartId);
		}

		Object collectionVarObject = globalContext.lookupVariable(CURRENT_OBJECT_VAR);
		ChartWorker chartWorker;
		if (collectionVarObject != null) {
			String collectionVar = (String) collectionVarObject;
			Object groupingObject = globalContext.lookupVariable(CURRENT_OBJECT);
			chartWorker = new ChartWorker(template, chartUuid, collectionVar, groupingObject, copyValuePairs, addValuePairs);
		} else {
			chartWorker = new ChartWorker(template, chartUuid, copyValuePairs, addValuePairs);
		}

		executorServiceHandler.submitToReportPool(chartWorker);
	}

	class ChartWorker implements Callable<SerializableBinding> {

		private ChartTemplate chartTemplate;
		private String chartUuid;
		private String groupingVar;
		private Object groupingObject;
		private List<Pair<String, String>> copyValuePairs;
		private List<Pair<String, String>> addValuePairs;

		ChartWorker(ChartTemplate chartTemplate, String chartUuid, List<Pair<String, String>> copyValuePairs, List<Pair<String, String>> addValuePairs) {
			this.chartTemplate = chartTemplate;
			this.chartUuid = chartUuid;
			this.copyValuePairs = copyValuePairs;
			this.addValuePairs = addValuePairs;
		}

		ChartWorker(ChartTemplate chartTemplate, String chartUuid, String groupingVar, Object groupingObject, List<Pair<String, String>> copyValuePairs, List<Pair<String, String>> addValuePairs) {
			this.chartTemplate = chartTemplate;
			this.chartUuid = chartUuid;
			this.groupingVar = groupingVar;
			this.groupingObject = groupingObject;
			this.copyValuePairs = copyValuePairs;
			this.addValuePairs = addValuePairs;
		}

		@Override
		public SerializableBinding call() throws Exception {
			ChartBuilder chartBuilder = new ChartBuilder(dataProvider, chartTemplate);
			if (groupingVar != null) {
				chartBuilder.addVariable(groupingVar, groupingObject);
			}

			for (Pair<String, String> valuePair : copyValuePairs) {
				chartBuilder.copyVariable(valuePair.getKey(), valuePair.getValue());
			}

			for (Pair<String, String> valuePair : addValuePairs) {
				chartBuilder.addVariable(valuePair.getKey(), valuePair.getValue());
			}

			ChartBinding binding = chartBuilder.generate();
			binding.setUuid(chartUuid);
			return binding;
		}
	}

}
