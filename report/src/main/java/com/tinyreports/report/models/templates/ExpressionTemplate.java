package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement
public class ExpressionTemplate {
    @XmlAttribute(required = true)
    private String var;
    @XmlValue
    private String expression;

    public ExpressionTemplate() {
    }

    public ExpressionTemplate(ExpressionTemplate expressionTemplate) {
        this.setVar(expressionTemplate.getVar());
        this.setExpression(expressionTemplate.getExpression());
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
