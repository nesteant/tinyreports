package com.tinyreports.report.models.templates;

import com.tinyreports.report.resolvers.RelationResolver;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Antonns
 * Date: 06.03.12
 * Time: 22:01
 */
@XmlRootElement(name = "relation")
public class RelationTemplate {
    @XmlAttribute
    private String columnId;
    @XmlAttribute
    private String expression;
    @XmlAttribute
    private String filter;
    @XmlAttribute(name = "resolverClass")
    private Class<? extends RelationResolver> relationResolverClass;

    public RelationTemplate() {
    }

    public RelationTemplate(RelationTemplate relationTemplate) {
        this.setColumnId(relationTemplate.getColumnId());
        this.setExpression(relationTemplate.getExpression());
        this.setFilter(relationTemplate.getFilter());
        this.setRelationResolverClass(relationTemplate.getRelationResolverClass());
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Class<? extends RelationResolver> getRelationResolverClass() {
        return relationResolverClass;
    }

    public void setRelationResolverClass(Class<? extends RelationResolver> relationResolverClass) {
        this.relationResolverClass = relationResolverClass;
    }
}
