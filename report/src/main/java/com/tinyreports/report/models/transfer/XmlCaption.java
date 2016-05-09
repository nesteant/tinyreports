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
