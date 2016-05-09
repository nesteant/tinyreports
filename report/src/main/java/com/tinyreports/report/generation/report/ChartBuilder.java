package com.tinyreports.report.generation.report;

import com.tinyreports.common.DataProvider;
import com.tinyreports.common.utils.ContextUtils;
import com.tinyreports.report.models.templates.ChartTemplate;
import com.tinyreports.report.models.transfer.ChartBinding;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ChartBuilder {
    private DataProvider dataProvider;
    private ChartTemplate chartTemplate;
    private StandardEvaluationContext context = new StandardEvaluationContext();

    public ChartBuilder(DataProvider dataProvider, ChartTemplate chartTemplate) {
        this.dataProvider = dataProvider;
        this.chartTemplate = chartTemplate;
    }

    public void copyVariable(String key, String value) {
        Object obj = dataProvider.getDataObjectByKey(key);
        context.setVariable(value, obj);
    }

    public void addVariable(String key, Object value) {
        context.setVariable(key, value);
    }

    public ChartBinding generate() {
        ChartBinding chartBinding = new ChartBinding();
        List<String> variables = chartTemplate.getVariables();
        if (variables != null) {
            for (String variable : variables) {
                context.setVariable(variable, dataProvider.getDataObjectByKey(variable));
            }
        }
//		String chartData = template.getChartData();
//		if (chartData != null) {
//			chartObject.setDataObject(parseVariable(chartData, context, dataProvider));
//		}
        String chartHeader = chartTemplate.getChartHeader();
        if (chartHeader != null) {
            chartBinding.setScript(parseVariable(chartHeader, context, dataProvider));
        }
        String chartContainer = chartTemplate.getChartContainer();
        if (chartContainer != null) {
            chartBinding.setContainer(parseVariable(chartContainer, context, dataProvider));
        }
        return chartBinding;
    }

    private String parseVariable(String variable, StandardEvaluationContext context, DataProvider dataProvider) {
        ContextUtils.loadToContext(context, null, dataProvider, variable);
        return ContextUtils.evaluateSafeExpression(context, variable, null);
    }
}
