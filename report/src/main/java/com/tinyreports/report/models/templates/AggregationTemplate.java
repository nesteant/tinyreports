package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Antonns
 * Date: 29.03.12
 * Time: 22:25
 */
@XmlRootElement(name = "aggregation")
public class AggregationTemplate extends AdditionalAttributeHolder {
    @XmlAttribute
    @Deprecated
    private String label;
    @XmlAttribute(name = "position")
    @Deprecated
    private String labelPosition;
    @XmlAttribute
    @Deprecated
    private List<String> columnIds;
    @XmlAttribute(name = "expression")
    @Deprecated
    private String aggregationExpression;
    @XmlAttribute(name = "function")
    @Deprecated
    private String aggregationFunction;
    @XmlAttribute(name = "format")
    @Deprecated
    private String aggregationFormat;
    @XmlElement(name = "aggrItem")
    private List<AggregationItem> aggregationItems;

    public AggregationTemplate() {
    }

    public AggregationTemplate(AggregationTemplate aggregationTemplate) {
        this.setLabel(aggregationTemplate.getLabel());
        this.setLabelPosition(aggregationTemplate.getLabelPosition());
        List<String> origCIds = aggregationTemplate.getColumnIds();
        if (origCIds != null) {
            this.setColumnIds(new ArrayList<String>(origCIds));
        }
        this.setAggregationExpression(aggregationTemplate.getAggregationExpression());
        this.setAggregationFunction(aggregationTemplate.getAggregationFunction());
        this.setAggregationFormat(aggregationTemplate.getAggregationFormat());
        List<AggregationItem> origAggregationItems = aggregationTemplate.getAggregationItems();
        if (origAggregationItems != null) {
            List<AggregationItem> copyAggregationItems = new ArrayList<AggregationItem>(origAggregationItems.size());
            for (AggregationItem origAggregationItem : origAggregationItems) {
                copyAggregationItems.add(new AggregationItem(origAggregationItem));
            }
            this.setAggregationItems(copyAggregationItems);
        }
        this.copyAttributes(aggregationTemplate.getAttributes());
    }

    @Deprecated
    public String getLabel() {
        return label;
    }

    @Deprecated
    public void setLabel(String label) {
        this.label = label;
    }

    @Deprecated
    public String getLabelPosition() {
        return labelPosition;
    }

    @Deprecated
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    @Deprecated
    public List<String> getColumnIds() {
        return columnIds;
    }

    @Deprecated
    public void setColumnIds(List<String> columnIds) {
        this.columnIds = columnIds;
    }

    @Deprecated
    public String getAggregationFunction() {
        return aggregationFunction;
    }

    @Deprecated
    public void setAggregationFunction(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction;
    }

    @Deprecated
    public String getAggregationFormat() {
        return aggregationFormat;
    }

    @Deprecated
    public void setAggregationFormat(String aggregationFormat) {
        this.aggregationFormat = aggregationFormat;
    }

    @Deprecated
    public String getAggregationExpression() {
        return aggregationExpression;
    }

    @Deprecated
    public void setAggregationExpression(String aggregationExpression) {
        this.aggregationExpression = aggregationExpression;
    }

    public List<AggregationItem> getAggregationItems() {
        return aggregationItems;
    }

    public void setAggregationItems(List<AggregationItem> aggregationItems) {
        this.aggregationItems = aggregationItems;
    }
}
