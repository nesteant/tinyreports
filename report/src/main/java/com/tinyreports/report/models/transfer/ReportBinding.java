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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "rpt-bnd")
public class ReportBinding implements SerializableBinding {

	@XmlAttribute(name = "rpt-id")
	private String uuid;

	@XmlElement(name = "xml-rpt")
	private XmlReport xmlReport;

	public ReportBinding() {
	}

	public ReportBinding(String reportUuid, XmlReport xmlReport) {
		this.uuid = reportUuid;
		this.xmlReport = xmlReport;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public XmlReport getXmlReport() {
		return xmlReport;
	}

	public void setXmlReport(XmlReport xmlReport) {
		this.xmlReport = xmlReport;
	}
}
