package com.tinyreports.report.models.transfer;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "p")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCaption {
    @XmlCDATA
    @XmlElement(name = "v")
    private String captionValue;
    //TODO fix
    @XmlCDATA
    @XmlElement(name = "a")
    private String alternativeCaption;

    public XmlCaption() {
    }

    public XmlCaption(String captionValue) {
        this.captionValue = captionValue;
    }

    public String getCaptionValue() {
        return captionValue;
    }

    public void setCaptionValue(String captionValue) {
        this.captionValue = captionValue;
    }

    public String getAlternativeCaption() {
        return alternativeCaption;
    }

    public void setAlternativeCaption(String alternativeCaption) {
        this.alternativeCaption = alternativeCaption;
    }
}
