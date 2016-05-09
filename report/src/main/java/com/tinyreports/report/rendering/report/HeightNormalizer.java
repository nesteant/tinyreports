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

package com.tinyreports.report.rendering.report;

import com.tinyreports.common.TemplateVariables;
import com.tinyreports.report.models.transfer.XmlCell;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class HeightNormalizer {

	public static void normalizeCellHeights(Map<String, List<XmlCell>> resolvingCollectionsMap, ReportTemplate template) {
		normalizeCellHeightsBackward(resolvingCollectionsMap, template);
		normalizeCellHeightsForward(resolvingCollectionsMap, template);
	}

	private static void normalizeCellHeightsBackward(Map<String, List<XmlCell>> resolvingCollectionsMap, ReportTemplate template) {

		LinkedList<String> collectionIterator = new LinkedList<String>();
		List<ColumnTemplate> columnTemplates = template.getColumns();
		collectionIterator.add(TemplateVariables.ITERATOR);
		for (ColumnTemplate columnTemplate : columnTemplates) {
			collectionIterator.add(columnTemplate.getId());
		}

		Iterator<String> descendingIterator = collectionIterator.descendingIterator();
		while (descendingIterator.hasNext()) {
			String currentId = descendingIterator.next();
			List<XmlCell> cellsOfCurrentColumn = resolvingCollectionsMap.get(currentId);

			for (XmlCell cell : cellsOfCurrentColumn) {
				calculateCellTemporarySize(cell);
			}
		}
	}

	private static void normalizeCellHeightsForward(Map<String, List<XmlCell>> resolvingCollectionsMap, ReportTemplate template) {

		List<ColumnTemplate> columnTemplates = template.getColumns();
		List<String> columnIds = new ArrayList<String>();
		columnIds.add(TemplateVariables.ITERATOR);
		for (ColumnTemplate columnTemplate : columnTemplates) {
			columnIds.add(columnTemplate.getId());
		}
		for (String columnId : columnIds) {

			List<XmlCell> currentCells = resolvingCollectionsMap.get(columnId);
			for (XmlCell cell : currentCells) {
				int cellHeight = cell.getCellHeight();

				Map<String, List<XmlCell>> childCellCollection = cell.getChildCellMap();
				for (String s : childCellCollection.keySet()) {
					Integer heightOfChildren = cell.getHeightOfChildren(s);

					Integer heightMultiplier = 1;
					Integer heightRest = 0;

					if (heightOfChildren < cellHeight) {

						heightMultiplier = cellHeight / heightOfChildren;
						heightRest = cellHeight % heightOfChildren;

						List<XmlCell> xmlCells = childCellCollection.get(s);
						for (XmlCell childCell : xmlCells) {
							childCell.setCellHeight(childCell.getCellHeight() * heightMultiplier);
						}
						Integer lastCellIndex = xmlCells.size() - 1;
						XmlCell lastXmlCell = xmlCells.get(lastCellIndex);
						lastXmlCell.setCellHeight(lastXmlCell.getCellHeight() + heightRest);
					}
				}
			}
		}
	}

	private static void calculateCellTemporarySize(XmlCell cell) {

		Map<String, List<XmlCell>> listOfChildCollections = cell.getChildCellMap();

		if (listOfChildCollections.size() == 0) {
			cell.setCellHeight(1);
		} else {

			Integer finalHeight = 0;
			for (String childCollectionColumnId : listOfChildCollections.keySet()) {

				//TODO probably we can change with cell method
				List<XmlCell> connectedCells = listOfChildCollections.get(childCollectionColumnId);
				Integer countHeight = evaluateHeightUsingChildList(connectedCells);

				if (countHeight != null) {
					if (finalHeight < countHeight) {
						finalHeight = countHeight;
					}
				} else {
					finalHeight = null;
					break;
				}
			}
			cell.setCellHeight(finalHeight);
		}
	}

	private static Integer evaluateHeightUsingChildList(List<XmlCell> reportCells) {

		Integer countHeight = 0;
		for (XmlCell connectedCell : reportCells) {
			if (connectedCell.getCellHeight() == null) {
				countHeight = null;
				break;
			} else {
				countHeight += connectedCell.getCellHeight();
			}
		}
		return countHeight;
	}
}