package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Antonns
 * Date: 03.04.12
 * Time: 19:41
 */
@XmlRootElement(name = "sorting")
public class ReportSortingTemplate {
    @XmlAttribute(required = true)
    private String columnId;
    @XmlAttribute
    private String printWhen;
    @XmlAttribute
    private Class valueType;
    @XmlAttribute
    private String conversionPattern;
    @XmlAttribute
    private String order = "asc";

    public ReportSortingTemplate() {
    }

    public ReportSortingTemplate(ReportSortingTemplate reportSortingTemplate) {
        this.setColumnId(reportSortingTemplate.getColumnId());
        this.setPrintWhen(reportSortingTemplate.getPrintWhen());
        this.setValueType(reportSortingTemplate.getValueType());
        this.setConversionPattern(reportSortingTemplate.getConversionPattern());
        this.setOrder(reportSortingTemplate.getOrder());
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getPrintWhen() {
        return printWhen;
    }

    public void setPrintWhen(String printWhen) {
        this.printWhen = printWhen;
    }

    public Class getValueType() {
        return valueType;
    }

    public void setValueType(Class valueType) {
        this.valueType = valueType;
    }

    public String getConversionPattern() {
        return conversionPattern;
    }

    public void setConversionPattern(String conversionPattern) {
        this.conversionPattern = conversionPattern;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
