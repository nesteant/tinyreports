package com.tinyreports.report.generation.layout;

import com.tinyreports.common.DataProvider;
import com.tinyreports.report.ExecutorServiceHandler;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Document;

import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class BuildInfo {

	private DataProvider dataProvider;
	private StandardEvaluationContext globalContext;
	private Document layout;
	private ExecutorServiceHandler executorServiceHandler;


	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public StandardEvaluationContext getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(StandardEvaluationContext globalContext) {
		this.globalContext = globalContext;
	}

	public Document getLayout() {
		return layout;
	}

	public void setLayout(Document layout) {
		this.layout = layout;
	}

	public ExecutorServiceHandler getExecutorServiceHandler() {
		return executorServiceHandler;
	}

	public void setExecutorServiceHandler(ExecutorServiceHandler executorServiceHandler) {
		this.executorServiceHandler = executorServiceHandler;
	}
}
