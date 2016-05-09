package com.tinyreports.common;

import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public abstract class XmlObject {
    protected Map<QName, String> additionalAttributes;

    public abstract void addAttribute(Node node);

    public abstract void addAttribute(String attrName, String attrValue);

    public Map<QName, String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void copyAttributes(Map<QName, String> attributes) {
        if (attributes == null) {
            return;
        }
        if (additionalAttributes == null) {
            additionalAttributes = new HashMap<QName, String>();
        }
        for (Map.Entry<QName, String> entry : attributes.entrySet()) {
            additionalAttributes.put(entry.getKey(), entry.getValue());
        }
    }
}
