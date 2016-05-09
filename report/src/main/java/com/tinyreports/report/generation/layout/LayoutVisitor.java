package com.tinyreports.report.generation.layout;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.TemplateVariables;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.common.utils.XmlSerializer;
import com.tinyreports.report.ExecutorServiceHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public abstract class LayoutVisitor implements Visitable {

	public static final String CURRENT_OBJECT = "CURRENT_OBJECT";
	public static final String CURRENT_OBJECT_VAR = "CURRENT_OBJECT_VAR";
	public static final String PRINT_WHEN_REPORT_ATTR = "printWhen";
	public static final String CHART_ID_ATTR = "chartId";
	public static final String UUID_ATTR = "uuid";
	public static final String ADD_VALUE = "addValue";
	public static final String COPY_VALUE = "copyValue";
	public static final String ID = "id";


	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	protected StandardEvaluationContext globalContext;
	protected Document layout;
	protected DataProvider dataProvider;
	protected ExecutorServiceHandler executorServiceHandler;
	protected BuildInfo buildInfo;


	protected LayoutVisitor(BuildInfo buildInfo) {
		this.buildInfo = buildInfo;
		globalContext = buildInfo.getGlobalContext();
		layout = buildInfo.getLayout();
		dataProvider = buildInfo.getDataProvider();
		executorServiceHandler = buildInfo.getExecutorServiceHandler();
	}

	protected <E> E evaluateLayoutExpression(String expression, Class<E> clazz) {
		ContextUtils.loadToContext(globalContext, null, dataProvider, expression);
		return (E) ContextUtils.evaluateNonSafeExpression(globalContext, expression, "", clazz);
	}

	protected <E> E evaluateLayoutExpression(StandardEvaluationContext ctx, DataProvider dataProvider, String expression, Class<E> clazz) {
		ContextUtils.loadToContext(ctx, null, dataProvider, expression);
		return (E) ContextUtils.evaluateNonSafeExpression(ctx, expression, "", clazz);
	}

	protected String evaluateNodeExpression(String expression) {
		if (expression.contains(TemplateVariables.EXPRESSION_CONTAINS_PATTERN)) {
			ContextUtils.loadToContext(globalContext, null, dataProvider, expression);
			String result = ContextUtils.evaluateSafeExpression(globalContext, expression, "");
			if (StringUtils.isNotEmpty(result)) {
				return result;
			}
			return StringUtils.EMPTY;
		} else {
			return expression;
		}
	}

	protected Node toNode(Document layout, Object obj) throws TinyReportException {
		Node n = layout.createElement("tmp");
		new XmlSerializer().serialize(obj, n);
		return n.getFirstChild();
	}

	public abstract void visit(Node n) throws TinyReportException, InterruptedException;


}
