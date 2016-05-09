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

import com.tinyreports.common.UniqueTemplate;
import com.tinyreports.common.adapters.ListAdapter;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * User: Antonns
 * Date: 28.03.12
 * Time: 23:10
 */
//TODO insert variables
@XmlRootElement(name = "chart")
public class ChartTemplate implements UniqueTemplate {

	@XmlAttribute
	private String id;

	@XmlAttribute
	@XmlJavaTypeAdapter(ListAdapter.class)
	private List<String> variables;

	@XmlElement(name = "chart-data")
	private String chartData;

	@XmlElement(name = "chart-header")
	private String chartHeader;

	@XmlElement(name = "chart-container")
	@XmlCDATA
	private String chartContainer;

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public String getChartHeader() {
		return chartHeader;
	}

	public void setChartHeader(String chartHeader) {
		this.chartHeader = chartHeader;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

	public String getChartContainer() {
		return chartContainer;
	}

	public void setChartContainer(String chartContainer) {
		this.chartContainer = chartContainer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
