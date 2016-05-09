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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
@XmlRootElement(name = "r")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlRow extends AdditionalAttributeHolder {

	@XmlElement(name = "c")
	private XmlCell iteratorCell;

	@XmlTransient
	private Object objectIterator;

	@XmlTransient
	private Boolean hasNonBlankValues = false;

	@XmlTransient
	private Map<String, List<XmlCell>> childCellMap;


	public List<XmlCell> accessColumn(String columnName) {
		if (childCellMap == null) {
			prepareCellMap();
		}
		return childCellMap.get(columnName);
	}

	public void prepareCellMap() {
		childCellMap = new HashMap<String, List<XmlCell>>();
		resolveChildren(iteratorCell);
	}

	private void resolveChildren(XmlCell xmlCell) {
		String cellColumnId = xmlCell.getColumnId();
		if (!childCellMap.containsKey(cellColumnId)) {
			childCellMap.put(cellColumnId, new ArrayList<XmlCell>());
		}
		childCellMap.get(cellColumnId).add(xmlCell);

		List<XmlCell> childCells = xmlCell.getChildCells();
		if (childCells == null || childCells.isEmpty()) {
		} else {
			for (XmlCell childCell : childCells) {
				resolveChildren(childCell);
			}
		}
	}

	public Integer getMaxHeight() {
		return iteratorCell.getCellHeight();

	}

	public Object getObjectIterator() {
		return objectIterator;
	}

	public void setObjectIterator(Object objectIterator) {
		this.objectIterator = objectIterator;
	}

	public XmlCell getIteratorCell() {
		return iteratorCell;
	}

	public void setIteratorCell(XmlCell iteratorCell) {
		this.iteratorCell = iteratorCell;
	}

	public Map<String, List<XmlCell>> getChildCellMap() {
		return childCellMap;
	}

	public void setChildCellMap(Map<String, List<XmlCell>> childCellMap) {
		this.childCellMap = childCellMap;
	}

	public Boolean getHasNonBlankValues() {
		return hasNonBlankValues;
	}

	public void setHasNonBlankValues(Boolean hasNonBlankValues) {
		this.hasNonBlankValues = hasNonBlankValues;
	}
}
