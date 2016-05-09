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
