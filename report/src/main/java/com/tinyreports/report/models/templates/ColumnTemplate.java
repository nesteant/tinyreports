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
 * Date: 06.03.12
 * Time: 22:00
 */
@XmlRootElement(name = "column")
public class ColumnTemplate extends AdditionalAttributeHolder {

	@XmlAttribute(name = "id")
	private String id;

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "hidden")
	private Boolean hidden;

	@XmlAttribute(name = "onError")
	private String errorExpression;

	@XmlElement (name = "relation")
	private RelationTemplate relationTemplate;

	@XmlElement (name = "value", required = true)
	private ValueTemplate valueTemplate;

	public ColumnTemplate() {
	}

	public ColumnTemplate(ColumnTemplate columnTemplate) {
		this.setId(columnTemplate.getId());
		this.setName(columnTemplate.getName());
		this.setHidden(columnTemplate.getHidden());
		this.setErrorExpression(columnTemplate.getErrorExpression());
		RelationTemplate origRt = columnTemplate.getRelationTemplate();
		if (origRt != null){
			this.setRelationTemplate(new RelationTemplate(origRt));
		}
		ValueTemplate origVt = columnTemplate.getValueTemplate();
		if (origVt != null){
			this.setValueTemplate(new ValueTemplate(origVt));
		}

		this.copyAttributes(columnTemplate.getAttributes());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RelationTemplate getRelationTemplate() {
		return relationTemplate;
	}

	public void setRelationTemplate(RelationTemplate relationTemplate) {
		this.relationTemplate = relationTemplate;
	}

	public ValueTemplate getValueTemplate() {
		return valueTemplate;
	}

	public void setValueTemplate(ValueTemplate valueTemplate) {
		this.valueTemplate = valueTemplate;
	}

	public String getErrorExpression() {
		return errorExpression;
	}

	public void setErrorExpression(String errorExpression) {
		this.errorExpression = errorExpression;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
}
