package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement
public class RowConfigTemplate {
    @XmlAttribute
    private String printWhen;

    public RowConfigTemplate() {
    }

    public RowConfigTemplate(RowConfigTemplate rowConfigTemplate) {
        this.setPrintWhen(rowConfigTemplate.getPrintWhen());
    }

    public String getPrintWhen() {
        return printWhen;
    }

    public void setPrintWhen(String printWhen) {
        this.printWhen = printWhen;
    }
}
