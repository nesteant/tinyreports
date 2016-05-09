package com.tinyreports.report.models.transfer;

import com.tinyreports.report.models.templates.AdditionalAttributeHolder;
import com.tinyreports.report.models.templates.ReportTemplate;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "r")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlReport extends AdditionalAttributeHolder {
    @XmlAttribute(name = "b")
    private Boolean isBlank;
    @XmlAttribute
    //TODO implement
    private boolean displayBlank = false;
    @XmlElement(name = "t")
    private String blankText;
    @XmlElement(name = "p")
    private XmlCaption xmlCaption;
    @XmlElementWrapper(name = "hs")
    @XmlElement(name = "h")
    private Queue<XmlHeader> xmlHeaders;
    @XmlElementWrapper(name = "rs")
    @XmlElement(name = "r")
    private List<XmlRow> xmlRows;
    @XmlElementWrapper(name = "as")
    @XmlElement(name = "a")
    private List<XmlRow> xmlAggregations;
    @XmlElement(name = "u")
    private String templateUuid;
    @XmlElement(name = "rt")
    private ReportTemplate reportTemplate;

    public synchronized void addRow(XmlRow xmlRow) {
        if (xmlRows == null) {
            xmlRows = Collections.synchronizedList(new ArrayList<XmlRow>());
            setXmlRows(xmlRows);
        }
        xmlRows.add(xmlRow);
    }

    public String getTemplateUuid() {
        return templateUuid;
    }

    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }

    public Boolean getBlank() {
        return isBlank;
    }

    public void setBlank(Boolean blank) {
        isBlank = blank;
    }

    public String getBlankText() {
        return blankText;
    }

    public void setBlankText(String blankText) {
        this.blankText = blankText;
    }

    public synchronized List<XmlRow> getXmlRows() {
        return xmlRows;
    }

    public synchronized void setXmlRows(List<XmlRow> xmlRows) {
        this.xmlRows = xmlRows;
    }

    public synchronized ReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    public synchronized void setReportTemplate(ReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public synchronized XmlCaption getXmlCaption() {
        return xmlCaption;
    }

    public synchronized void setXmlCaption(XmlCaption xmlCaption) {
        this.xmlCaption = xmlCaption;
    }

    public synchronized Queue<XmlHeader> getXmlHeaders() {
        return xmlHeaders;
    }

    public synchronized void setXmlHeaders(Queue<XmlHeader> xmlHeaders) {
        this.xmlHeaders = xmlHeaders;
    }

    public List<XmlRow> getXmlAggregations() {
        return xmlAggregations;
    }

    public void setXmlAggregations(List<XmlRow> xmlAggregations) {
        this.xmlAggregations = xmlAggregations;
    }

    public void setAlternativeCaption(String alternativeCaption) {
        xmlCaption.setAlternativeCaption(alternativeCaption);
    }

    public boolean isDisplayBlank() {
        return displayBlank;
    }

    public void setDisplayBlank(boolean displayBlank) {
        this.displayBlank = displayBlank;
    }
}
