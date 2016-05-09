package com.tinyreports.report.models.templates;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Anton Nesterenko
 * @since 0.5.5
 */
@XmlRootElement(name = "html")
public class HtmlTemplate {
    @XmlValue
    @XmlCDATA
    private String html;

    public HtmlTemplate() {
    }

    public HtmlTemplate(HtmlTemplate htmlTemplate) {
        this.html = htmlTemplate.getHtml();
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
