package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Antonns
 * Date: 06.03.12
 * Time: 22:00
 */
@XmlRootElement(name = "column")
public class ColumnTemplate extends AdditionalAttributeHolder {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "hidden")
    private Boolean hidden;
    @XmlAttribute(name = "onError")
    private String errorExpression;
    @XmlElement(name = "relation")
    private RelationTemplate relationTemplate;
    @XmlElement(name = "value", required = true)
    private ValueTemplate valueTemplate;

    public ColumnTemplate() {
    }

    public ColumnTemplate(ColumnTemplate columnTemplate) {
        this.setId(columnTemplate.getId());
        this.setName(columnTemplate.getName());
        this.setHidden(columnTemplate.getHidden());
        this.setErrorExpression(columnTemplate.getErrorExpression());
        RelationTemplate origRt = columnTemplate.getRelationTemplate();
        if (origRt != null) {
            this.setRelationTemplate(new RelationTemplate(origRt));
        }
        ValueTemplate origVt = columnTemplate.getValueTemplate();
        if (origVt != null) {
            this.setValueTemplate(new ValueTemplate(origVt));
        }
        this.copyAttributes(columnTemplate.getAttributes());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelationTemplate getRelationTemplate() {
        return relationTemplate;
    }

    public void setRelationTemplate(RelationTemplate relationTemplate) {
        this.relationTemplate = relationTemplate;
    }

    public ValueTemplate getValueTemplate() {
        return valueTemplate;
    }

    public void setValueTemplate(ValueTemplate valueTemplate) {
        this.valueTemplate = valueTemplate;
    }

    public String getErrorExpression() {
        return errorExpression;
    }

    public void setErrorExpression(String errorExpression) {
        this.errorExpression = errorExpression;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
