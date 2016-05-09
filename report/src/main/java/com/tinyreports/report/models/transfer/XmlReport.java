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

import com.tinyreports.report.models.templates.AdditionalAttributeHolder;
import com.tinyreports.report.models.templates.ReportTemplate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */

@XmlRootElement(name = "r")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlReport extends AdditionalAttributeHolder {

	@XmlAttribute(name = "b")
	private Boolean isBlank;

	@XmlAttribute
	//TODO implement
	private boolean displayBlank = false;

	@XmlElement(name = "t")
	private String blankText;

	@XmlElement(name = "p")
	private XmlCaption xmlCaption;

	@XmlElementWrapper(name = "hs")
	@XmlElement(name = "h")
	private Queue<XmlHeader> xmlHeaders;

	@XmlElementWrapper(name = "rs")
	@XmlElement(name = "r")
	private List<XmlRow> xmlRows;

	@XmlElementWrapper(name = "as")
	@XmlElement(name = "a")
	private List<XmlRow> xmlAggregations;

	@XmlElement(name = "u")
	private String templateUuid;

	@XmlElement(name = "rt")
	private ReportTemplate reportTemplate;

	public synchronized void addRow(XmlRow xmlRow) {
		if (xmlRows == null) {
			xmlRows = Collections.synchronizedList(new ArrayList<XmlRow>());
			setXmlRows(xmlRows);
		}
		xmlRows.add(xmlRow);
	}

	public String getTemplateUuid() {
		return templateUuid;
	}

	public void setTemplateUuid(String templateUuid) {
		this.templateUuid = templateUuid;
	}

	public Boolean getBlank() {
		return isBlank;
	}

	public void setBlank(Boolean blank) {
		isBlank = blank;
	}

	public String getBlankText() {
		return blankText;
	}

	public void setBlankText(String blankText) {
		this.blankText = blankText;
	}

	public synchronized List<XmlRow> getXmlRows() {
		return xmlRows;
	}

	public synchronized void setXmlRows(List<XmlRow> xmlRows) {
		this.xmlRows = xmlRows;
	}

	public synchronized ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public synchronized void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public synchronized XmlCaption getXmlCaption() {
		return xmlCaption;
	}

	public synchronized void setXmlCaption(XmlCaption xmlCaption) {
		this.xmlCaption = xmlCaption;
	}

	public synchronized Queue<XmlHeader> getXmlHeaders() {
		return xmlHeaders;
	}

	public synchronized void setXmlHeaders(Queue<XmlHeader> xmlHeaders) {
		this.xmlHeaders = xmlHeaders;
	}

	public List<XmlRow> getXmlAggregations() {
		return xmlAggregations;
	}

	public void setXmlAggregations(List<XmlRow> xmlAggregations) {
		this.xmlAggregations = xmlAggregations;
	}

	public void setAlternativeCaption(String alternativeCaption) {
		xmlCaption.setAlternativeCaption(alternativeCaption);
	}

	public boolean isDisplayBlank() {
		return displayBlank;
	}

	public void setDisplayBlank(boolean displayBlank) {
		this.displayBlank = displayBlank;
	}
}
