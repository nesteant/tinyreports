package com.tinyreports.report.models.transfer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "crt-bnd")
public class ChartBinding implements SerializableBinding {
    @XmlAttribute(name = "c-id")
    private String uuid;
    @XmlElement(name = "scr")
    private String script;
    @XmlElement(name = "cntr")
    private String container;

    @Override
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
