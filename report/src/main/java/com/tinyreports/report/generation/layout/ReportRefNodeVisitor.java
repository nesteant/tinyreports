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
import com.tinyreports.common.utils.ArrayUtils;
import com.tinyreports.common.utils.CollectionUtils;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.generation.report.ReportBuilder;
import com.tinyreports.report.models.templates.ReportRefTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.models.transfer.ReportBinding;
import com.tinyreports.report.models.transfer.SerializableBinding;
import com.tinyreports.report.models.transfer.XmlReport;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class ReportRefNodeVisitor extends LayoutVisitor implements Visitable {

	protected ReportRefNodeVisitor(BuildInfo buildInfo) {
		super(buildInfo);
	}

	@Override
	public void visit(Node n) throws TinyReportException, InterruptedException {
		ReportRefTemplate refTemplate = new XmlDeserializer<ReportRefTemplate>(ReportRefTemplate.class).deserialize(n);

		String ref = refTemplate.getRef();
		if (StringUtils.isEmpty(ref)) {
			throw new TinyReportTemplateException("Report ref should contain attribute REF");
		}

		String printWhenExpression = refTemplate.getPrintWhenExpression();
		boolean shouldPrint = true;
		if (!StringUtils.isEmpty(printWhenExpression)) {
			shouldPrint = evaluateLayoutExpression(globalContext, dataProvider, printWhenExpression, Boolean.class);
		}

		if (shouldPrint) {
			ReportTemplate rt = (ReportTemplate) dataProvider.getDataObjectByKey(ref);
			if (rt == null) {
				throw new TinyReportTemplateException("Template %s not found in dataProvider", refTemplate.getRef());
			}

			ReportTemplate copy = new ReportTemplate(rt);

			Map<QName, String> attributes = refTemplate.getAttributes();
			if (attributes != null){
				for (QName qName : attributes.keySet()) {
					String val = attributes.get(qName);
					if ((val.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN))) {
						String newVal = StringEscapeUtils.escapeHtml(evaluateNodeExpression(val));
						attributes.put(qName, newVal);
					}
				}
			}

			copy.copyAttributes(attributes);

			String uuid = UUID.randomUUID().toString();
			((Element) n).setAttribute("uuid", uuid);
			scheduleReportGeneration(uuid, refTemplate, copy);
		} else {
			Node parent = n.getParentNode();
			parent.removeChild(n);
		}
	}

	private void scheduleReportGeneration(String uuid, ReportRefTemplate refTemplate, ReportTemplate reportTemplate) throws TinyReportTemplateException, InterruptedException {
		String whenBlankExpression = StringUtils.EMPTY;

		if (StringUtils.isNotEmpty(refTemplate.getWhenBlankExpression())) {
			whenBlankExpression = refTemplate.getWhenBlankExpression();
		} else if (StringUtils.isNotEmpty(reportTemplate.getWhenBlankExpression())) {
			whenBlankExpression = reportTemplate.getWhenBlankExpression();
		}

		if (StringUtils.isNotEmpty(whenBlankExpression)) {
			if (whenBlankExpression.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
				ContextUtils.loadToContext(globalContext, null, dataProvider, whenBlankExpression);
				whenBlankExpression = (String) ContextUtils.evaluateNonSafeExpression(globalContext, whenBlankExpression, "", String.class);
			}
		}

		String caption = refTemplate.getCaption();
		if (StringUtils.isNotEmpty(caption)) {
			if (caption.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
				ContextUtils.loadToContext(globalContext, null, dataProvider, caption);
				caption = (String) ContextUtils.evaluateNonSafeExpression(globalContext, caption, "", String.class);
			}
		}
		Object currentObject;

		String iterator = refTemplate.getIterator();
		boolean hasIterator = StringUtils.isNotEmpty(iterator);

		if (hasIterator) {
			if (iterator.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
				ContextUtils.loadToContext(globalContext, null, dataProvider, iterator);
				currentObject = ContextUtils.evaluateNonSafeExpression(globalContext, iterator);
			} else {
				currentObject = dataProvider.getDataObjectByKey(iterator);
			}
		} else {
			currentObject = dataProvider.getDataObjectByKey(reportTemplate.getIteratorId());
		}
		//TODO exceptionHandling
		if (currentObject == null) {
			throw new TinyReportTemplateException("Failed to receive iterator both from expressions and dataProvider");
		}

		Map<String,Object> groupVars = new HashMap<String, Object>();
		List<String> variables = refTemplate.getVariables();
		if (CollectionUtils.isNotEmpty(variables)){
			for (String variable : variables) {
				Object o = globalContext.lookupVariable(variable);
				groupVars.put(variable, o);
			}
		}
		ReferenceReportWorker reportWorker = new ReferenceReportWorker(reportTemplate, currentObject, uuid,
				caption, whenBlankExpression, groupVars);
		executorServiceHandler.submitToReportPool(reportWorker);
	}

	class ReferenceReportWorker implements Callable<SerializableBinding> {

		private ReportTemplate reportTemplate;
		private Object workerObject;
		private String reportUuid;
		private String alternativeCaption;
		private String whenBlankExpression;
		private Map<String, Object> groupVars;

		ReferenceReportWorker(ReportTemplate reportTemplate, Object workerObject, String reportUuid,
							  String alternativeCaption, String whenBlankExpression, Map<String,Object> groupVars) {
			this.reportTemplate = reportTemplate;
			this.workerObject = workerObject;
			this.reportUuid = reportUuid;
			this.alternativeCaption = alternativeCaption;
			this.whenBlankExpression = whenBlankExpression;
			this.groupVars = groupVars;
		}

		@Override
		public SerializableBinding call() throws Exception {
			ReportBuilder reportBuilder = new ReportBuilder(dataProvider, reportTemplate, groupVars);
			try {
				List<Object> objs = ArrayUtils.linearalizeArrayOfObjects(workerObject);
				generateByRows(reportBuilder, objs);
			} catch (Exception e) {
				//TODO exception handling
				executorServiceHandler.reportReportException(e, true);
				throw e;
			}
			XmlReport xmlReport = reportBuilder.getXmlReport();
			if (StringUtils.isNotEmpty(alternativeCaption)) {
				xmlReport.setAlternativeCaption(StringEscapeUtils.escapeHtml(alternativeCaption));
			}
			if (StringUtils.isNotEmpty(whenBlankExpression)) {
				xmlReport.setBlankText(whenBlankExpression);
			}
			return new ReportBinding(reportUuid, xmlReport);
		}

		private void generateByRows(final ReportBuilder reportBuilder, Collection iterators) throws InterruptedException {
//			Set<String> uuidToWait = Collections.synchronizedSet(new HashSet<String>(iterators.size()));
			Set<String> uuidToWait = new CopyOnWriteArraySet<String>();
			for (final Object iteratorObject : iterators) {

				String uuid = executorServiceHandler.executeInRowPool(new Runnable() {
					@Override
					public void run() {
						try {
							reportBuilder.generateXmlRow(iteratorObject);
						} catch (Exception e) {
							LOGGER.error("Exception in thread", e);
							executorServiceHandler.reportRowException(e, true);
						}
					}
				});
				uuidToWait.add(uuid);
			}

			//TODO possible we need to add infinity check
			while (uuidToWait.size() > 0) {
				for (String uuid : uuidToWait) {
					if (executorServiceHandler.isRowTaskFinished(uuid)) {
						uuidToWait.remove(uuid);
					}
				}
			}
			reportBuilder.finalizeGeneration(iterators);
		}
	}

}
