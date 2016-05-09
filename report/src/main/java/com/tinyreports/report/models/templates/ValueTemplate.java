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

import com.tinyreports.common.adapters.ListAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Antonns
 * Date: 23.04.12
 * Time: 15:17
 */
@XmlRootElement(name = "value")
public class ValueTemplate extends AdditionalAttributeHolder {

	@XmlAttribute (name = "whenBlank")
	private String blankExpression;

	@XmlAttribute
	@XmlJavaTypeAdapter(ListAdapter.class)
	private List<String> escapeTags;

	@XmlAttribute
	@XmlJavaTypeAdapter(ListAdapter.class)
	private List<String> tagFilters;

	@XmlElement(name = "text")
	private String textContent;

	@XmlElement(name = "html")
	private HtmlTemplate htmlTemplate;

	public ValueTemplate() {
	}

	public ValueTemplate(ValueTemplate valueTemplate) {
		this.setBlankExpression(valueTemplate.getBlankExpression());
		List<String> escapeTags = valueTemplate.getEscapeTags();
		if (escapeTags != null) {
			this.setEscapeTags(new ArrayList<String>(valueTemplate.getEscapeTags()));
		}
		List<String> tagFilters = valueTemplate.getTagFilters();
		if (tagFilters != null) {
			this.setTagFilters(new ArrayList<String>(valueTemplate.getTagFilters()));
		}

		this.setTextContent(valueTemplate.getTextContent());
		HtmlTemplate origHtmlTemplate = valueTemplate.getHtmlTemplate();
		if (origHtmlTemplate != null) {
			this.setHtmlTemplate(new HtmlTemplate(origHtmlTemplate));
		}

		this.copyAttributes(valueTemplate.getAttributes());
	}

	public Boolean isTagContent() {
		return !getHtmlValue().isEmpty();
	}

	public String getContent() {
		if (!getHtmlValue().isEmpty()) {
			return getHtmlValue();
		} else {
			return textContent;
		}
	}

	public String getHtmlValue() {
		if (htmlTemplate != null && htmlTemplate.getHtml() != null) {
			return htmlTemplate.getHtml();
		}
		return "";
	}

	public String getBlankExpression() {
		return blankExpression;
	}

	public void setBlankExpression(String blankExpression) {
		this.blankExpression = blankExpression;
	}

	public List<String> getEscapeTags() {
		return escapeTags;
	}

	public void setEscapeTags(List<String> escapeTags) {
		this.escapeTags = escapeTags;
	}

	public List<String> getTagFilters() {
		return tagFilters;
	}

	public void setTagFilters(List<String> tagFilters) {
		this.tagFilters = tagFilters;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public HtmlTemplate getHtmlTemplate() {
		return htmlTemplate;
	}

	public void setHtmlTemplate(HtmlTemplate htmlTemplate) {
		this.htmlTemplate = htmlTemplate;
	}
}
