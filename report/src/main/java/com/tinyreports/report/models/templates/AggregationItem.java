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
 * @author Anton Nesterenko
 * @since 0.5.5
 */
@XmlRootElement(name = "aggrItem")
public class AggregationItem extends AdditionalAttributeHolder {

	@XmlAttribute
	private String columnId;

	@XmlAttribute (name = "expression")
	private String aggregationExpression;

	@XmlAttribute (name = "function")
	private String aggregationFunction;

	@XmlAttribute  (name = "format")
	private String aggregationFormat;

	public AggregationItem() {
	}

	public AggregationItem(AggregationItem aggregationItem) {
		this.setColumnId(aggregationItem.getColumnId());
		this.setAggregationExpression(aggregationItem.getAggregationExpression());
		this.setAggregationFunction(aggregationItem.getAggregationFunction());
		this.setAggregationFormat(aggregationItem.getAggregationFormat());
		this.copyAttributes(aggregationItem.getAttributes());
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getAggregationExpression() {
		return aggregationExpression;
	}

	public void setAggregationExpression(String aggregationExpression) {
		this.aggregationExpression = aggregationExpression;
	}

	public String getAggregationFunction() {
		return aggregationFunction;
	}

	public void setAggregationFunction(String aggregationFunction) {
		this.aggregationFunction = aggregationFunction;
	}

	public String getAggregationFormat() {
		return aggregationFormat;
	}

	public void setAggregationFormat(String aggregationFormat) {
		this.aggregationFormat = aggregationFormat;
	}
}
