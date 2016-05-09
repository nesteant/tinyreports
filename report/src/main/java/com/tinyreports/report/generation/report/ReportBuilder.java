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

package com.tinyreports.report.generation.report;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import com.tinyreports.common.utils.CollectionUtils;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.ObjectUtils;
import com.tinyreports.report.AggregationFunction;
import com.tinyreports.report.models.templates.*;
import com.tinyreports.report.models.transfer.XmlCaption;
import com.tinyreports.report.models.transfer.XmlCell;
import com.tinyreports.report.models.transfer.XmlHeader;
import com.tinyreports.report.models.transfer.XmlReport;
import com.tinyreports.report.models.transfer.XmlRow;
import com.tinyreports.report.resolvers.RelationResolver;
import com.tinyreports.report.resolvers.ResolverFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ReportBuilder {

	private static Logger LOGGER = LoggerFactory.getLogger(ReportBuilder.class);

	private XmlReport xmlReport;

	private DataProvider dataProvider;

	private ReportTemplate template;

	private ConcurrentHashMap<String, String> expressionMap;
	private Map<String, Object> groupVars;

	public ReportBuilder(XmlReport xmlReport) throws TinyReportTemplateException {
		this.xmlReport = xmlReport;
		init();
	}

//	public ReportBuilder(DataProvider dataProvider, ReportTemplate template) {
//		xmlReport = new XmlReport();
//		xmlReport.setReportTemplate(template);
//
//		this.dataProvider = dataProvider;
//		this.template = template;
//	}

	public ReportBuilder(DataProvider dataProvider, ReportTemplate template,Map<String, Object> groupVars) {
		xmlReport = new XmlReport();
		xmlReport.setReportTemplate(template);

		this.dataProvider = dataProvider;
		this.template = template;
		this.groupVars = groupVars;
	}

	private void init() throws TinyReportTemplateException {
		template = xmlReport.getReportTemplate();
		if (template == null) {
			String captionValue = xmlReport.getXmlCaption().getCaptionValue();
			throw new TinyReportTemplateException("Template cannot be found in XmlReport \"%s\"", captionValue);
		}
	}

	public void generate(Collection<?> iterators) throws TinyReportTemplateException {

		if (template == null) {
			throw new TinyReportTemplateException("Template Not Found");
		}

		if (xmlReport.getReportTemplate() == null) {
			xmlReport.setReportTemplate(template);
		}

		for (Object iterator : iterators) {
			generateXmlRow(iterator);
		}
		finalizeGeneration(iterators);
	}

	public XmlReport finalizeGeneration(Object iterator) {
		calculateReportValues(iterator);
		resolveAggregations(iterator);

		List<XmlRow> xmlRows = xmlReport.getXmlRows();
		if (CollectionUtils.isNotEmpty(xmlRows)) {
			xmlReport.setBlank(false);
		} else {
			xmlReport.setBlank(true);
			String whenBlankExpression = xmlReport.getReportTemplate().getWhenBlankExpression();
			if (StringUtils.isNotEmpty(whenBlankExpression)) {
				StandardEvaluationContext rowCtx = newGroupContext();
				ContextUtils.loadToContext(rowCtx, expressionMap, dataProvider, whenBlankExpression);
				String text = (String) ContextUtils.evaluateNonSafeExpression(rowCtx, whenBlankExpression, "", String.class);
				xmlReport.setBlankText(text);
			}
		}

		ReportSortingTemplate[] sortObjects = xmlReport.getReportTemplate().getSortObjects();
		List<ReportSortingTemplate> filteredSortObjects = new ArrayList<ReportSortingTemplate>();
		for (ReportSortingTemplate sortObject : sortObjects) {
			StandardEvaluationContext ctx = new StandardEvaluationContext();
			String expression = sortObject.getPrintWhen();
			if (StringUtils.isNotBlank(expression)) {
				ContextUtils.loadToContext(ctx, null, dataProvider, expression);
				Boolean result = (Boolean) ContextUtils.evaluateNonSafeExpression(ctx, expression, "#{false}", Boolean.class);
				if (result) {
					filteredSortObjects.add(sortObject);
				}
			} else {
				filteredSortObjects.add(sortObject);
			}
			xmlReport.getReportTemplate().setSortObjects(filteredSortObjects
					.toArray(new ReportSortingTemplate[filteredSortObjects.size()]));
		}

		return xmlReport;
	}

	////////////////////////////////////////	Generator	/////////////////////////////////////////////////
	public void generateXmlRow(Object iteratorObject) {

		if (xmlReport.getReportTemplate() == null) {
			xmlReport.setReportTemplate(template);
		}

		//TODO define best way. Remove from here
		//TODO define correct exception handling
		List<ExpressionTemplate> expressionTemplates = xmlReport.getReportTemplate().getExpressionTemplates();
		if (expressionTemplates != null && expressionMap == null) {
			expressionMap = new ConcurrentHashMap<String, String>();
			for (ExpressionTemplate expressionTemplate : expressionTemplates) {
				String expr = expressionTemplate.getExpression();
				String exprId = expressionTemplate.getVar();
				expressionMap.put(exprId, expr);
			}
		}

		XmlRow xmlRow = new XmlRow();

		modelXmlRow(xmlRow, iteratorObject);
		calculateXmlRow(xmlRow);

		RowConfigTemplate rowConfigTemplate = xmlReport.getReportTemplate().getRowConfigTemplate();
		Boolean shouldPrint = true;
		if (rowConfigTemplate != null) {
			String printWhenExpression = rowConfigTemplate.getPrintWhen();
			StandardEvaluationContext rowCtx = newGroupContext();
			ContextUtils.loadToContext(rowCtx, expressionMap, dataProvider, printWhenExpression);
			rowCtx.setVariable("row", xmlRow);
			shouldPrint = (Boolean) ContextUtils.evaluateNonSafeExpression(rowCtx, printWhenExpression, "", Boolean.class);
		}

		if (shouldPrint && xmlRow.getHasNonBlankValues()) {
			xmlReport.addRow(xmlRow);
		}
	}

	private void modelXmlRow(XmlRow xmlRow, Object iteratorObject) {

		xmlRow.setObjectIterator(iteratorObject);
		XmlCell iteratorCell = new XmlCell(iteratorObject, null);
		iteratorCell.setColumnId(TemplateVariables.ITERATOR);
		xmlRow.setIteratorCell(iteratorCell);

		List<ColumnTemplate> columns = template.getColumns();

		Map<String, List<XmlCell>> resolvingCellsMap = new HashMap<String, List<XmlCell>>();
		List<XmlCell> iteratorCells = new ArrayList<XmlCell>();
		iteratorCells.add(iteratorCell);
		resolvingCellsMap.put(TemplateVariables.ITERATOR, iteratorCells);

		for (ColumnTemplate column : columns) {
			List<XmlCell> resolvedCells = new ArrayList<XmlCell>();

			RelationTemplate relationTemplate = column.getRelationTemplate();

			List<XmlCell> resolvingCells = resolvingCellsMap.get(relationTemplate.getColumnId());
			// TODO Null pointer in case column name is wrong
			for (XmlCell resolvingCell : resolvingCells) {
				List<XmlCell> resolvedXmlCellsPartList;
				try {
					RelationResolver resolver = ResolverFactory.getResolver(relationTemplate.getRelationResolverClass());
					resolver.setGroupVars(groupVars);
//					List<Object> resolvedObjects = resolver.receiveRelatedObjects(column, iteratorObject, resolvingCell.getObjectValue(), dataProvider);
					List<Object> resolvedObjects = resolver.receiveRelatedObjects(column, expressionMap, iteratorObject, resolvingCell, dataProvider);

					resolvedXmlCellsPartList = wrapResolvedObjects(resolvedObjects, resolvingCell, column.getId());

					resolvedCells.addAll(resolvedXmlCellsPartList);
				} catch (SpelEvaluationException e) {
					LOGGER.error(String.format("%s %s", template.getId(), column.getId()));
					throw e;
				}
			}

			if (!xmlRow.getHasNonBlankValues()) {
				for (XmlCell resolvedCell : resolvedCells) {
					if (!ObjectUtils.instanceOfBlankObject(resolvedCell.getObjectValue())) {
						xmlRow.setHasNonBlankValues(true);
						break;
					}
				}
			}
			resolvingCellsMap.put(column.getId(), resolvedCells);
			xmlRow.setChildCellMap(resolvingCellsMap);
		}
	}

	private void calculateXmlRow(XmlRow xmlRow) {
		Map<String, List<XmlCell>> registeredCellsMap = xmlRow.getChildCellMap();

		List<ColumnTemplate> columnTemplates = template.getColumns();
		for (ColumnTemplate columnTemplate : columnTemplates) {

			List<XmlCell> xmlCells = registeredCellsMap.get(columnTemplate.getId());
			for (XmlCell xmlCell : xmlCells) {
				calculateCell(xmlRow, xmlCell, columnTemplate);
			}
		}
	}

	private void calculateCell(XmlRow xmlRow, XmlCell xmlCell, ColumnTemplate columnTemplate) {

		ValueTemplate valueTemplate = columnTemplate.getValueTemplate();
		xmlCell.setTagContent(valueTemplate.isTagContent());
		xmlCell.copyAttributes(valueTemplate.getAttributes());
		calculateAttributes(xmlCell);
		Object currentCellValue = xmlCell.getObjectValue();

		String expression;
		if (ObjectUtils.instanceOfBlankObject(currentCellValue)) {
			String blankExpression = valueTemplate.getBlankExpression();
			if (blankExpression == null) {
				return;
			} else {
				expression = blankExpression;
			}
		} else {
			expression = valueTemplate.getContent();
		}

		if (expression == null) {
			expression = StringUtils.EMPTY;
		}

		StandardEvaluationContext ctx = newGroupContext();
		ctx.setVariable(TemplateVariables.CELL_OBJECT, xmlCell);
		ctx.setVariable(TemplateVariables.ITERATOR, xmlRow.getObjectIterator());
		ctx.setVariable(TemplateVariables.LOGIC_ROW, xmlRow);
		ctx.setVariable(TemplateVariables.CURRENT_OBJECT, currentCellValue);

		ContextUtils.loadToContext(ctx, expressionMap, dataProvider, expression);

		String result = ContextUtils.evaluateSafeExpression(ctx, expression, null);
		xmlCell.setContent(result);
	}

	private List<XmlCell> wrapResolvedObjects(List<Object> objects, XmlCell parentCell, String currentColumnId) {

		List<XmlCell> cells = new ArrayList<XmlCell>();

		if (CollectionUtils.isEmpty(objects)) {
			XmlCell cellImpl = new XmlCell(ObjectUtils.createBlankObject(), parentCell);
			cellImpl.setColumnId(currentColumnId);
			cells.add(cellImpl);

			//TODO make common method
			Set<String> relatedColumnIds = parentCell.getRelatedColumnIds();
			if (relatedColumnIds == null) {
				parentCell.setRelatedColumnIds(new HashSet<String>());
			}
			parentCell.getRelatedColumnIds().add(currentColumnId);
		} else {
			for (Object object : objects) {
				XmlCell cellImpl = new XmlCell(object, parentCell);
				cellImpl.setColumnId(currentColumnId);
				Set<String> relatedColumnIds = parentCell.getRelatedColumnIds();
				if (relatedColumnIds == null) {
					parentCell.setRelatedColumnIds(new HashSet<String>());
				}
				parentCell.getRelatedColumnIds().add(currentColumnId);
				cells.add(cellImpl);
			}
		}

		if (parentCell.getChildCells() == null) {
			parentCell.setChildCells(cells);
		} else {
			parentCell.getChildCells().addAll(cells);
		}
		return cells;
	}

	//////////////////////////////////////////////	Calculator	/////////////////////////////////////////////////////

	public void calculateReportValues(Object iterator) {
		LOGGER.trace("Start report model calculation");
		calculateReportAttributes();
		calculateCaption(iterator);
		calculateHeader();
		LOGGER.trace("Report calculation finished");
	}

	private void calculateAttributes(AutoClassAssignable object) {
		String id = object.getId();
		String searchClass = "class";
		object.addAttribute(searchClass, id);
	}

	private void calculateReportAttributes() {

		Map<QName, String> attributes = template.getAttributes();
		xmlReport.copyAttributes(attributes);
		xmlReport.addAttribute("class", template.getId());
	}

	private void calculateHeader() {
		Queue<XmlHeader> xmlHeaders = new ConcurrentLinkedQueue<XmlHeader>();

		List<ColumnTemplate> columnTemplates = template.getColumns();
		for (ColumnTemplate columnTemplate : columnTemplates) {

			String columnId = columnTemplate.getId();

			XmlHeader xmlHeader = new XmlHeader();
			xmlHeader.setColumnId(columnId);
			xmlHeader.setValue(columnTemplate.getName());
			xmlHeader.setHidden(columnTemplate.getHidden());
			xmlHeader.copyAttributes(columnTemplate.getAttributes());
			calculateAttributes(xmlHeader);
			xmlHeaders.add(xmlHeader);
		}
		xmlReport.setXmlHeaders(xmlHeaders);
	}

	private void calculateCaption(Object iterator) {
		StandardEvaluationContext context = newGroupContext();

		context.setVariable(TemplateVariables.CAPTION_OBJECT, template.getCaption());
		context.setVariable(TemplateVariables.ITERATOR, iterator);
		HtmlTemplate captionContent = template.getCaption().getCaptionContent();
		if (captionContent != null) {
			String captionFormatterExpression = ContextUtils.evaluateNonSafeExpression(context, captionContent.getHtml(), null);
			xmlReport.setXmlCaption(new XmlCaption(captionFormatterExpression));
		} else {
			xmlReport.setXmlCaption(new XmlCaption(template.getCaption().getName()));
		}
	}


	private StandardEvaluationContext prepareContext(XmlCell cellImpl, DataProvider dataProvider, String expression, Object iterator) {
		StandardEvaluationContext ctx = newGroupContext();
		ContextUtils.loadToContext(ctx, expressionMap, dataProvider, expression);
		ctx.setVariable(TemplateVariables.CELL_OBJECT, cellImpl);
		ctx.setVariable(TemplateVariables.ITERATOR, iterator);
		return ctx;
	}

	///////////////////////////////////////	Aggregation	/////////////////////////////////////////////////////////

	public void resolveAggregations(Object iterator) {

		List<XmlRow> aggregationRows = new ArrayList<XmlRow>();
		List<AggregationTemplate> aggregationTemplates = template.getAggregationTemplates();
		if (aggregationTemplates == null) {
			return;
		}
		List<ColumnTemplate> columnTemplates = template.getColumns();
		for (AggregationTemplate aggregationTemplate : aggregationTemplates) {
			XmlRow aggregationRow = new XmlRow();

			aggregationRow.copyAttributes(aggregationTemplate.getAttributes());

			XmlCell iteratorCell = new XmlCell(null, null);
			iteratorCell.setCellHeight(1);
			iteratorCell.setColumnId(TemplateVariables.ITERATOR);
			iteratorCell.setChildCells(new ArrayList<XmlCell>());

			aggregationRow.setIteratorCell(iteratorCell);

			List<AggregationItem> aggregationItems = new ArrayList<AggregationItem>();

			if (CollectionUtils.isEmpty(aggregationTemplate.getAggregationItems())) {
				List<String> columnIds = aggregationTemplate.getColumnIds();

				for (String columnId : columnIds) {
					AggregationItem aggregationItem = new AggregationItem();
					aggregationItem.setColumnId(columnId);
					aggregationItem.setAggregationExpression(aggregationTemplate.getAggregationExpression());
					aggregationItem.setAggregationFunction(aggregationTemplate.getAggregationFunction());
					aggregationItem.setAggregationFormat(aggregationTemplate.getAggregationFormat());
					aggregationItems.add(aggregationItem);
				}

				if (aggregationTemplate.getLabel() != null) {
					AggregationItem aggregationItem = new AggregationItem();
					aggregationItem.setColumnId(aggregationTemplate.getLabelPosition());
					aggregationItem.setAggregationExpression(aggregationTemplate.getLabel());
					aggregationItems.add(aggregationItem);
				}
			} else {
				aggregationItems = aggregationTemplate.getAggregationItems();
			}

			for (ColumnTemplate columnTemplate : columnTemplates) {
				String columnTemplateId = columnTemplate.getId();

				XmlCell currentCell = new XmlCell(null, iteratorCell);
				currentCell.setCellHeight(1);
				currentCell.setColumnId(columnTemplateId);


				AggregationItem currentItem = null;
				for (AggregationItem aggregationItem : aggregationItems) {
					if (aggregationItem.getColumnId().equals(columnTemplateId)) {
						currentItem = aggregationItem;
						break;
					}
				}
				if (currentItem != null) {
					currentCell.copyAttributes(currentItem.getAttributes());

					List<XmlRow> xmlRows = xmlReport.getXmlRows();
					List<XmlCell> allCellsOfCurrentColumn = new ArrayList<XmlCell>();
					if (xmlRows != null) {
						for (XmlRow xmlRow : xmlRows) {
							//TODO find way not to call this method
							xmlRow.prepareCellMap();
							List<XmlCell> affectedCellImpls = xmlRow.getChildCellMap().get(columnTemplateId);
							allCellsOfCurrentColumn.addAll(affectedCellImpls);
						}
					}

					//TODO implement different approach of execution expressions
					String aggregationResult;
					String aggregationExpression = currentItem.getAggregationExpression();

					if (StringUtils.isNotEmpty(aggregationExpression)) {
						StandardEvaluationContext ctx = newGroupContext();
						ContextUtils.loadToContext(ctx, expressionMap, dataProvider, aggregationExpression);
						ctx.setVariable(TemplateVariables.ITERATOR, iterator);
						aggregationResult = ContextUtils.evaluateSafeExpression(ctx, aggregationExpression, "");
					} else {
						//TODO define check of cast
						AggregationFunction function = AggregationFunction.valueOf(currentItem.getAggregationFunction());
						aggregationResult = function.executeAggregationFunction(allCellsOfCurrentColumn, currentItem.getAggregationFormat());
					}

					currentCell.setContent(aggregationResult);
				}
				iteratorCell.getChildCells().add(currentCell);
			}

			aggregationRow.prepareCellMap();

			aggregationRows.add(aggregationRow);
		}
		xmlReport.setXmlAggregations(aggregationRows);
	}

	public XmlReport getXmlReport() {
		return xmlReport;
	}

	private StandardEvaluationContext newGroupContext(){
		StandardEvaluationContext ctx = new StandardEvaluationContext();
		ctx.setVariables(groupVars);
		return ctx;
	}
}
