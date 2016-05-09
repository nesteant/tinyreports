package com.tinyreports.report.models.transfer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "rpt-bnd")
public class ReportBinding implements SerializableBinding {
    @XmlAttribute(name = "rpt-id")
    private String uuid;
    @XmlElement(name = "xml-rpt")
    private XmlReport xmlReport;

    public ReportBinding() {
    }

    public ReportBinding(String reportUuid, XmlReport xmlReport) {
        this.uuid = reportUuid;
        this.xmlReport = xmlReport;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public XmlReport getXmlReport() {
        return xmlReport;
    }

    public void setXmlReport(XmlReport xmlReport) {
        this.xmlReport = xmlReport;
    }
}
