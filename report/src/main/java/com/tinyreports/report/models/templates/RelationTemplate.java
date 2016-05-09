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

import com.tinyreports.report.resolvers.RelationResolver;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Antonns
 * Date: 06.03.12
 * Time: 22:01
 */

@XmlRootElement(name = "relation")
public class RelationTemplate {

	@XmlAttribute
	private String columnId;

	@XmlAttribute
	private String expression;

	@XmlAttribute
	private String filter;

	@XmlAttribute(name = "resolverClass")
	private Class<? extends RelationResolver> relationResolverClass;

	public RelationTemplate() {
	}

	public RelationTemplate(RelationTemplate relationTemplate) {
		this.setColumnId(relationTemplate.getColumnId());
		this.setExpression(relationTemplate.getExpression());
		this.setFilter(relationTemplate.getFilter());
		this.setRelationResolverClass(relationTemplate.getRelationResolverClass());
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Class<? extends RelationResolver> getRelationResolverClass() {
		return relationResolverClass;
	}

	public void setRelationResolverClass(Class<? extends RelationResolver> relationResolverClass) {
		this.relationResolverClass = relationResolverClass;
	}
}
