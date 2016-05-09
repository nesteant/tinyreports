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

import com.tinyreports.report.models.transfer.XmlCell;
import com.tinyreports.report.models.transfer.XmlReport;
import com.tinyreports.report.models.transfer.XmlRow;
import com.tinyreports.report.models.templates.ReportSortingTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class HtmlReportSortingService {

	protected static Boolean checkSortIsApplied(ReportTemplate template) {
		return ArrayUtils.isNotEmpty(template.getSortObjects());
	}

	private static ReportSortingTemplate getSortingTemplate(String columnId, ReportSortingTemplate[] sortObjects) {
		for (ReportSortingTemplate sortObject : sortObjects) {
			if (sortObject.getColumnId().equals(columnId)) {
				return sortObject;
			}
		}
		return null;
	}

	public static void provideInnerSorting(XmlRow xmlRow, ReportTemplate template) {

		if (checkSortIsApplied(template)) {
			ReportSortingTemplate[] sortingTemplates = template.getSortObjects();

			XmlCell iteratorCell = xmlRow.getIteratorCell();

			recursivelySortChildCells(iteratorCell, sortingTemplates);
		}
	}

	private static void recursivelySortChildCells(XmlCell xmlCell, ReportSortingTemplate[] sortingTemplates) {

		Set<String> relatedColumnIds = xmlCell.getRelatedColumnIds();

		if (relatedColumnIds == null) {
			return;
		}

		for (String relatedColumnId : relatedColumnIds) {
			ReportSortingTemplate sortingTemplate = getSortingTemplate(relatedColumnId, sortingTemplates);
			if (sortingTemplate != null) {
				ModelSorter.sortCellCollectionByField(xmlCell.getChildCells(), sortingTemplate);
			}
		}

		List<XmlCell> childCellImpls = xmlCell.getChildCells();
		if (childCellImpls == null) {
			return;
		}
		for (XmlCell childCellImpl : childCellImpls) {
			recursivelySortChildCells(childCellImpl, sortingTemplates);
		}
	}

	public static void provideOuterSorting(XmlReport xmlReport) {

		ReportTemplate reportTemplate = xmlReport.getReportTemplate();
		ReportSortingTemplate[] sortObjects = reportTemplate.getSortObjects();
		if (checkSortIsApplied(reportTemplate)) {
			ModelSorter.outerSorting(xmlReport.getXmlRows(), sortObjects);
		}
	}
}
