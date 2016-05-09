package com.tinyreports.report.models.templates;

import com.tinyreports.common.UniqueTemplate;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Antonns
 * Date: 06.03.12
 * Time: 21:59
 */
@XmlRootElement(name = "report")
public class ReportTemplate extends AdditionalAttributeHolder implements UniqueTemplate {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String printWhenExpression;
    @XmlAttribute(name = "iterator")
    private String iteratorId;
    @XmlAttribute(name = "whenBlank")
    private String whenBlankExpression;
    @XmlElementWrapper(name = "expressions")
    @XmlElement(name = "expression")
    private List<ExpressionTemplate> expressionTemplates;
    @XmlElement(name = "rowConfiguration")
    private RowConfigTemplate rowConfigTemplate;
    @XmlElement
    private CaptionTemplate caption;
    @XmlList
    @XmlElement(name = "sort")
    private ReportSortingTemplate[] sortObjects;
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    private List<ColumnTemplate> columns;
    @XmlElementWrapper(name = "aggregations")
    @XmlElement(name = "aggregation")
    private List<AggregationTemplate> aggregationTemplates;

    public ReportTemplate() {
    }

    public ReportTemplate(ReportTemplate reportTemplate) {
        this.setId(reportTemplate.getId());
        this.setPrintWhenExpression(reportTemplate.getPrintWhenExpression());
        this.setIteratorId(reportTemplate.getIteratorId());
        this.setWhenBlankExpression(reportTemplate.getWhenBlankExpression());
        List<ExpressionTemplate> origExpTemplates = reportTemplate.getExpressionTemplates();
        if (origExpTemplates != null) {
            List<ExpressionTemplate> copyExpTemplates = new ArrayList<ExpressionTemplate>(origExpTemplates.size());
            for (ExpressionTemplate origExpTemplate : origExpTemplates) {
                copyExpTemplates.add(new ExpressionTemplate(origExpTemplate));
            }
            this.setExpressionTemplates(copyExpTemplates);
        }
        RowConfigTemplate origRowConfigTemplate = reportTemplate.getRowConfigTemplate();
        if (origRowConfigTemplate != null) {
            this.setRowConfigTemplate(new RowConfigTemplate(origRowConfigTemplate));
        }
        CaptionTemplate origCaption = reportTemplate.getCaption();
        if (origCaption != null) {
            this.setCaption(reportTemplate.getCaption());
        }
        ReportSortingTemplate[] origRsts = reportTemplate.getSortObjects();
        if (origRsts != null) {
            ReportSortingTemplate[] copyRsts = new ReportSortingTemplate[origRsts.length];
            for (int i = 0; i < origRsts.length; i++) {
                copyRsts[i] = new ReportSortingTemplate(origRsts[i]);
            }
            this.setSortObjects(copyRsts);
        }
        List<ColumnTemplate> origColumns = reportTemplate.getColumns();
        if (origColumns != null) {
            List<ColumnTemplate> copyColumns = new ArrayList<ColumnTemplate>(origColumns.size());
            for (ColumnTemplate origColumn : origColumns) {
                copyColumns.add(new ColumnTemplate(origColumn));
            }
            this.setColumns(copyColumns);
        }
        List<AggregationTemplate> origAggrTempls = reportTemplate.getAggregationTemplates();
        if (origAggrTempls != null) {
            List<AggregationTemplate> copyAggrTempls = new ArrayList<AggregationTemplate>(origAggrTempls.size());
            for (AggregationTemplate origAggrTempl : origAggrTempls) {
                copyAggrTempls.add(new AggregationTemplate(origAggrTempl));
            }
            this.setAggregationTemplates(copyAggrTempls);
        }
        this.copyAttributes(reportTemplate.getAttributes());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIteratorId() {
        return iteratorId;
    }

    public void setIteratorId(String iteratorId) {
        this.iteratorId = iteratorId;
    }

    public String getWhenBlankExpression() {
        return whenBlankExpression;
    }

    public void setWhenBlankExpression(String whenBlankExpression) {
        this.whenBlankExpression = whenBlankExpression;
    }

    public List<ExpressionTemplate> getExpressionTemplates() {
        return expressionTemplates;
    }

    public void setExpressionTemplates(List<ExpressionTemplate> expressionTemplates) {
        this.expressionTemplates = expressionTemplates;
    }

    public RowConfigTemplate getRowConfigTemplate() {
        return rowConfigTemplate;
    }

    public void setRowConfigTemplate(RowConfigTemplate rowConfigTemplate) {
        this.rowConfigTemplate = rowConfigTemplate;
    }

    public CaptionTemplate getCaption() {
        return caption;
    }

    public void setCaption(CaptionTemplate caption) {
        this.caption = caption;
    }

    public ReportSortingTemplate[] getSortObjects() {
        return sortObjects;
    }

    public void setSortObjects(ReportSortingTemplate[] sortObjects) {
        this.sortObjects = sortObjects;
    }

    public List<ColumnTemplate> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnTemplate> columns) {
        this.columns = columns;
    }

    public List<AggregationTemplate> getAggregationTemplates() {
        return aggregationTemplates;
    }

    public void setAggregationTemplates(List<AggregationTemplate> aggregationTemplates) {
        this.aggregationTemplates = aggregationTemplates;
    }

    public String getPrintWhenExpression() {
        return printWhenExpression;
    }

    public void setPrintWhenExpression(String printWhenExpression) {
        this.printWhenExpression = printWhenExpression;
    }
}
