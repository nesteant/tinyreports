/*
 * Tinyreports
 * Copyright (c) 2013. Anton Nesterenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
