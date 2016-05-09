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
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.ObjectUtils;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.transfer.XmlCell;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ElResolver extends RelationResolver {

    @Override
    public List<Object> getRelatedObjects(ColumnTemplate columnTemplate, Map<String, String> expressionMap, Object iteratorObject, XmlCell resolvingCell, DataProvider dataProvider) {

        if (ObjectUtils.instanceOfBlankObject(resolvingCell.getObjectValue())) {
            return new ArrayList<Object>();
        }

        StandardEvaluationContext context = newGroupContext();

        String expression = columnTemplate.getRelationTemplate().getExpression();

        context.setVariable(TemplateVariables.DATA_PROVIDER, dataProvider);
        context.setVariable(TemplateVariables.ITERATOR, iteratorObject);
        context.setVariable(TemplateVariables.RESOLVING_CELL_OBJECT, resolvingCell);
        context.setVariable(TemplateVariables.CURRENT_OBJECT, resolvingCell.getObjectValue());
        ContextUtils.loadToContext(context, expressionMap, dataProvider, expression);

        return (List) ContextUtils.evaluateNonSafeExpression(context, expression, columnTemplate.getErrorExpression(), List.class);
    }

    private StandardEvaluationContext newGroupContext() {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariables(groupVars);
        return ctx;
    }
}
