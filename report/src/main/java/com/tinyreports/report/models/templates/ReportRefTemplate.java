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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */
@XmlRootElement(name = "reportRef")
public class ReportRefTemplate extends AdditionalAttributeHolder {

	@XmlAttribute
	private String ref;

	@XmlAttribute
	private String caption;

	@XmlAttribute (name = "printWhen")
	private String printWhenExpression;

	@XmlAttribute(name = "iterator")
	private String iterator;

	@XmlAttribute(name = "whenBlank")
	private String whenBlankExpression;

	@XmlAttribute
	@XmlJavaTypeAdapter(value = ListAdapter.class)
	private List<String> variables;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getPrintWhenExpression() {
		return printWhenExpression;
	}

	public void setPrintWhenExpression(String printWhenExpression) {
		this.printWhenExpression = printWhenExpression;
	}

	public String getIterator() {
		return iterator;
	}

	public void setIterator(String iterator) {
		this.iterator = iterator;
	}

	public String getWhenBlankExpression() {
		return whenBlankExpression;
	}

	public void setWhenBlankExpression(String whenBlankExpression) {
		this.whenBlankExpression = whenBlankExpression;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}
}
