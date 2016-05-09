package com.tinyreports.report.models.layout;

import org.eclipse.persistence.oxm.annotations.XmlReadOnly;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
@XmlRootElement(name = "grouping")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupingElement {
    @XmlAttribute
    private String uuid;
    @XmlAttribute(required = true)
    private String collection;
    @XmlAttribute(required = true)
    private String var;
    @XmlElementWrapper(name = "sorting")
    @XmlElement(name = "sort")
    @XmlReadOnly
    private List<SortingElement> sortings;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public List<SortingElement> getSortings() {
        return sortings;
    }

    public void setSortings(List<SortingElement> sortings) {
        this.sortings = sortings;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
