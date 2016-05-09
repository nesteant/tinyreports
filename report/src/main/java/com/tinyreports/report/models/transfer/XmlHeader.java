package com.tinyreports.report.models.transfer;

import com.tinyreports.report.models.templates.AdditionalAttributeHolder;
import com.tinyreports.report.models.templates.AutoClassAssignable;

import javax.xml.bind.annotation.*;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "h")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlHeader extends AdditionalAttributeHolder implements AutoClassAssignable {
    @XmlValue
    private String value;
    @XmlAttribute(name = "h")
    private Boolean hidden;
    @XmlAttribute(name = "i")
    private String columnId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    @Override
    public String getId() {
        return columnId;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
