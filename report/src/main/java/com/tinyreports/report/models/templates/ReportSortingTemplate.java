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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Antonns
 * Date: 03.04.12
 * Time: 19:41
 */
@XmlRootElement(name = "sorting")
public class ReportSortingTemplate {

	@XmlAttribute (required = true)
	private String columnId;

	@XmlAttribute
	private String printWhen;

	@XmlAttribute
	private Class valueType;

	@XmlAttribute
	private String conversionPattern;

	@XmlAttribute
	private String order = "asc";

	public ReportSortingTemplate() {
	}

	public ReportSortingTemplate(ReportSortingTemplate reportSortingTemplate) {
		this.setColumnId(reportSortingTemplate.getColumnId());
		this.setPrintWhen(reportSortingTemplate.getPrintWhen());
		this.setValueType(reportSortingTemplate.getValueType());
		this.setConversionPattern(reportSortingTemplate.getConversionPattern());
		this.setOrder(reportSortingTemplate.getOrder());
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getPrintWhen() {
		return printWhen;
	}

	public void setPrintWhen(String printWhen) {
		this.printWhen = printWhen;
	}

	public Class getValueType() {
		return valueType;
	}

	public void setValueType(Class valueType) {
		this.valueType = valueType;
	}

	public String getConversionPattern() {
		return conversionPattern;
	}

	public void setConversionPattern(String conversionPattern) {
		this.conversionPattern = conversionPattern;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
