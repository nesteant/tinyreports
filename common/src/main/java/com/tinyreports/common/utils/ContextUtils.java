package com.tinyreports.common.utils;

import com.tinyreports.common.DataProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ContextUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(ContextUtils.class);
    public static final Pattern VAR_PATTERN = Pattern.compile("#([^\\W]+)");
    public static final ExpressionParser parser = new SpelExpressionParser();
    public static final TemplateParserContext parserContext = new TemplateParserContext();

    public static Boolean loadToContext(StandardEvaluationContext standardEvaluationContext, Map<String, String> expressionMap, DataProvider dataProvider, String expression) {
        loadStandardFunctions(standardEvaluationContext);
        Matcher matcher = VAR_PATTERN.matcher(expression);
        while (matcher.find()) {
            String variable = matcher.group(1);
            //TODO this block should just amend expression but not execute it
            if (expressionMap != null && expressionMap.containsKey(variable)) {
                String expressionString = expressionMap.get(variable);
                loadToContext(standardEvaluationContext, expressionMap, dataProvider, expressionString);
                Object o = evaluateNonSafeExpression(standardEvaluationContext, expressionString);
                standardEvaluationContext.setVariable(variable, o);
            }
            Object dataObjectByKey = dataProvider.getDataObjectByKey(variable);
            if (dataObjectByKey != null) {
                standardEvaluationContext.setVariable(variable, dataObjectByKey);
            }
        }
        return true;
    }

    public static Object evaluateNonSafeExpression(StandardEvaluationContext context, String expression) {
        return parser.parseExpression(expression, parserContext).getValue(context);
    }

    public static String evaluateNonSafeExpression(StandardEvaluationContext context, String expression, String errorExpression) {
        return (String) evaluateNonSafeExpression(context, expression, errorExpression, String.class);
    }

    public static Object evaluateNonSafeExpression(StandardEvaluationContext context, String expression, String errorExpression, Class returnType) {
        try {
            return parser.parseExpression(expression, parserContext).getValue(context, returnType);
        } catch (EvaluationException e) {
            LOGGER.error("Expression {} cannot be resolved. Trying to resolve with specified error expression: {}", new Object[]{expression, errorExpression, e});
            if (errorExpression == null) {
                throw e;
            }
            return parser.parseExpression(errorExpression, parserContext).getValue(context, returnType);
        }
    }

    public static String evaluateSafeExpression(StandardEvaluationContext context, String expression, String errorExpression) {
        try {
            return evaluateNonSafeExpression(context, expression, errorExpression);
        } catch (Exception e) {
            LOGGER.error("Expression {} cannot be resolved", expression, e);
            return "";
        }
    }

    public static void loadStandardFunctions(StandardEvaluationContext ctx) {
        try {
            ctx.setVariable("color", ContextUtils.class.getMethod("color", String.class, Element.class));
            ctx.setVariable("colorRow", ContextUtils.class.getMethod("colorRow", String.class, List.class));
        } catch (NoSuchMethodException e) {
            LOGGER.error("error occurred", e);
        }
    }

    public static void color(String color, Element htmlElement) {
        String attrVal = htmlElement.getAttribute("style");
        attrVal = attrVal.concat("background:").concat(color);
        htmlElement.setAttribute("style", attrVal);
    }

    //TODO redefine with new concept
    public static void colorRow(String color, List<Element> row) {
        for (Element element : row) {
            String attrVal = element.getAttribute("style");
            attrVal = attrVal.concat("background:").concat(color);
            element.setAttribute("style", attrVal);
        }
    }

    public static String toJsonStringArray(Collection<String> objects) {
        String joinedCollection = StringUtils.join(objects, "','");
        return String.format("['%s']", joinedCollection);
    }

    public static String toJsonValueArray(Collection<String> objects) {
        String joinedCollection = StringUtils.join(objects, ",");
        return String.format("[%s]", joinedCollection);
    }
}
