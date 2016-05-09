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

package com.tinyreports.report.resolvers;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.utils.ArrayUtils;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.templates.RelationTemplate;
import com.tinyreports.report.models.transfer.XmlCell;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public abstract class RelationResolver {

	protected Map<String,Object> groupVars;

	public List<Object> receiveRelatedObjects(ColumnTemplate columnTemplate, Map<String, String> expressionMap, Object iteratorObject, XmlCell resolvingCell, DataProvider dataProvider) {

		List<Object> relatedObjects = getRelatedObjects(columnTemplate, expressionMap, iteratorObject, resolvingCell, dataProvider);

		RelationTemplate relationTemplate = columnTemplate.getRelationTemplate();

		if (relationTemplate.getFilter() != null) {
			relatedObjects = filterRelatedObjects(columnTemplate, expressionMap, relatedObjects, dataProvider, iteratorObject, resolvingCell);
		}
		return relatedObjects;
	}

	public abstract List<Object> getRelatedObjects(ColumnTemplate columnTemplate, Map<String, String> expressionMap, Object iteratorObject, XmlCell resolvingCell, DataProvider dataProvider);

	private List<Object> filterRelatedObjects(ColumnTemplate columnTemplate, Map<String, String> expressionMap, List<Object> relatedObjects, DataProvider dataProvider, Object iterator, XmlCell resolvingCell) {
		String filter = columnTemplate.getRelationTemplate().getFilter();
		StandardEvaluationContext context = new StandardEvaluationContext();

		ContextUtils.loadToContext(context, expressionMap, dataProvider, filter);
		context.setVariable(TemplateVariables.ITERATOR, iterator);
		context.setVariable(TemplateVariables.RESOLVING_CELL_OBJECT, resolvingCell);
		context.setVariable(TemplateVariables.RELATED_OBJECT, resolvingCell.getObjectValue());
		context.setVariable(TemplateVariables.RELATED_OBJECTS, relatedObjects);

		Object filterResult = ContextUtils.evaluateNonSafeExpression(context, filter, columnTemplate.getErrorExpression(), Object.class);
		relatedObjects = ArrayUtils.linearalizeArrayOfObjects(filterResult);
		return relatedObjects;
	}

	public void setGroupVars(Map<String, Object> groupVars) {
		this.groupVars = groupVars;
	}
}
