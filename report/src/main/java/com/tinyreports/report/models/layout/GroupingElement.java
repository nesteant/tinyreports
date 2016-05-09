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

package com.tinyreports.report.models.layout;

import org.eclipse.persistence.oxm.annotations.XmlReadOnly;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 2.0
 */

@XmlRootElement(name = "grouping")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupingElement {
	@XmlAttribute
	private String uuid;

	@XmlAttribute(required = true)
	private String collection;

	@XmlAttribute(required = true)
	private String var;

	@XmlElementWrapper(name = "sorting")
	@XmlElement(name = "sort")
	@XmlReadOnly
	private List<SortingElement> sortings;

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public List<SortingElement> getSortings() {
		return sortings;
	}

	public void setSortings(List<SortingElement> sortings) {
		this.sortings = sortings;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
