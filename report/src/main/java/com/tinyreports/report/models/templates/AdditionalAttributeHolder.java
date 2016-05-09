package com.tinyreports.report.models.templates;

import com.tinyreports.common.TemplateVariables;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public abstract class AdditionalAttributeHolder {
    @XmlAnyAttribute
    protected Map<QName, String> outerAttrs;

    public void addAttribute(String attrName, String attrValue) {
        if (attrValue.isEmpty()) {
            return;
        }
        if (attrName.startsWith("xmlns:") || attrName.startsWith("xsi:")) {
            return;
        }
        if (outerAttrs == null) {
            outerAttrs = new HashMap<QName, String>();
        }
        if (attrName.equals(TemplateVariables.CLASS_ATTR)) {
            QName qName = getAttr(TemplateVariables.CLASS_ATTR);
            if (qName != null) {
                String currentVal = outerAttrs.get(qName);
                if (currentVal.contains(attrValue)) {
                    return;
                }
                currentVal = String.format("%s %s", currentVal, attrValue);
                outerAttrs.put(qName, currentVal);
                return;
            }
        }
        outerAttrs.put(new QName(null, attrName), attrValue);
    }

    public void copyAttributesToElement(Element element) {
        if (outerAttrs != null) {
            for (QName qName : outerAttrs.keySet()) {
                String newAttrValue = outerAttrs.get(qName);
                if (qName.getLocalPart().equals("class")) {
                    if (element.hasAttribute(qName.getLocalPart())) {
                        String classValue = element.getAttribute(qName.getLocalPart());
                        if (!classValue.contains(newAttrValue)) {
                            element.setAttribute(qName.getLocalPart(), classValue.concat(" " + newAttrValue));
                        }
                        return;
                    }
                }
                element.setAttribute(qName.getLocalPart(), newAttrValue);
            }
        }
    }

    public void copyAttributes(Map<QName, String> attributes) {
        if (attributes == null) {
            return;
        }
        if (outerAttrs == null) {
            outerAttrs = new HashMap<QName, String>();
        }
        for (Map.Entry<QName, String> entry : attributes.entrySet()) {
            outerAttrs.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<QName, String> getAttributes() {
        return outerAttrs;
    }

    private QName getAttr(String attr) {
        for (QName qName : outerAttrs.keySet()) {
            if (qName.getLocalPart().equals(attr)) {
                return qName;
            }
        }
        return null;
    }
}
