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

	@XmlElement (name = "html")
	private HtmlTemplate captionContent;

	public CaptionTemplate() {
	}

	public CaptionTemplate(CaptionTemplate captionTemplate) {
		this.setName(captionTemplate.getName());
		this.setDisplayCaption(captionTemplate.getDisplayCaption());
		this.setDisplayHeader(captionTemplate.getDisplayHeader());

		HtmlTemplate origHt = captionTemplate.getCaptionContent();
		if (origHt != null){
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
