package com.tinyreports.report.models.templates;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Antonns
 * Date: 13.03.12
 * Time: 18:39
 */
@XmlRootElement(name = "caption")
public class CaptionTemplate extends AdditionalAttributeHolder {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private Boolean displayCaption;
    @XmlAttribute
    private Boolean displayHeader;
    @XmlElement(name = "html")
    private HtmlTemplate captionContent;

    public CaptionTemplate() {
    }

    public CaptionTemplate(CaptionTemplate captionTemplate) {
        this.setName(captionTemplate.getName());
        this.setDisplayCaption(captionTemplate.getDisplayCaption());
        this.setDisplayHeader(captionTemplate.getDisplayHeader());
        HtmlTemplate origHt = captionTemplate.getCaptionContent();
        if (origHt != null) {
            this.setCaptionContent(captionTemplate.getCaptionContent());
        }
        this.copyAttributes(captionTemplate.getAttributes());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDisplayCaption() {
        return displayCaption;
    }

    public void setDisplayCaption(Boolean displayCaption) {
        this.displayCaption = displayCaption;
    }

    public Boolean getDisplayHeader() {
        return displayHeader;
    }

    public void setDisplayHeader(Boolean displayHeader) {
        this.displayHeader = displayHeader;
    }

    public HtmlTemplate getCaptionContent() {
        return captionContent;
    }

    public void setCaptionContent(HtmlTemplate captionContent) {
        this.captionContent = captionContent;
    }
}
