package com.tinyreports.report.models.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
@XmlRootElement(name = "sort")
@XmlAccessorType(XmlAccessType.FIELD)
public class SortingElement {
    @XmlAttribute
    private String field;
    @XmlAttribute
    private String order = "asc";
    @XmlAttribute
    private String value;

    public SortingElement() {
    }

    public SortingElement(SortingElement sortingElement) {
        this.field = sortingElement.field;
        this.order = sortingElement.order;
        this.value = sortingElement.value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
