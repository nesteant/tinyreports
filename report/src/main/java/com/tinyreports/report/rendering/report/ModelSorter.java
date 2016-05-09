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
import com.tinyreports.report.models.transfer.XmlRow;
import com.tinyreports.report.models.templates.ReportSortingTemplate;
import com.tinyreports.report.rendering.AlphanumComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ModelSorter {

	public static void sortCellCollectionByField(List<XmlCell> collectionOfObjects, ReportSortingTemplate sortingTemplate) {
		Collections.sort(collectionOfObjects, new CellComparator(sortingTemplate));
	}

	public static void outerSorting(List<XmlRow> xmlRows, ReportSortingTemplate[] sortingTemplates) {
		Collections.sort(xmlRows, new NewXmlRowSorter(sortingTemplates));
	}

	public static void sortTableResultObjectCollectionByField(List<XmlRow> xmlRows, ReportSortingTemplate sortingTemplate) {
		Collections.sort(xmlRows, new XmlRowSorter(sortingTemplate));
	}

	static class NewXmlRowSorter implements Comparator<XmlRow> {

		private ReportSortingTemplate[] sortingTemplates;

		NewXmlRowSorter(ReportSortingTemplate[] sortingTemplates) {
			this.sortingTemplates = sortingTemplates;
		}

		@Override
		public int compare(XmlRow row1, XmlRow row2) {
			for (ReportSortingTemplate sortingTemplate : sortingTemplates) {
				String columnId = sortingTemplate.getColumnId();
				List<XmlCell> comparableList1 = row1.accessColumn(columnId);
				sortCellCollectionByField(comparableList1, sortingTemplate);
				List<XmlCell> comparableList2 = row2.accessColumn(columnId);
				sortCellCollectionByField(comparableList2, sortingTemplate);
				int compared = new CellComparator(sortingTemplate).compare(comparableList1.get(0), comparableList2.get(0));
				if (compared != 0) {
					return compared;
				}
			}
			return 0;
		}
	}

	private static class XmlRowSorter implements Comparator<XmlRow> {
		private ReportSortingTemplate sortingTemplate;

		private XmlRowSorter(ReportSortingTemplate sortingTemplate) {
			this.sortingTemplate = sortingTemplate;
		}

		@Override
		public int compare(XmlRow row1, XmlRow row2) {
			String columnId = sortingTemplate.getColumnId();
			List<XmlCell> comparableList1 = row1.accessColumn(columnId);
			sortCellCollectionByField(comparableList1, sortingTemplate);
			List<XmlCell> comparableList2 = row2.accessColumn(columnId);
			sortCellCollectionByField(comparableList2, sortingTemplate);
			return new CellComparator(sortingTemplate).compare(comparableList1.get(0), comparableList2.get(0));
		}
	}

	private static class CellComparator implements Comparator<XmlCell> {

		private ReportSortingTemplate sortingTemplate;

		private CellComparator(ReportSortingTemplate sortingTemplate) {
			this.sortingTemplate = sortingTemplate;
		}

		@Override
		public int compare(XmlCell o1, XmlCell o2) {
			String sortingColumn = sortingTemplate.getColumnId();
			if (!o1.getColumnId().equals(sortingColumn) || !o2.getColumnId().equals(sortingColumn)) {
				return 0;
			}
			String value1 = o1.getContent();
			String value2 = o2.getContent();
			return compareWithOrder(value1, value2, sortingTemplate.getOrder());
		}

		private int compareWithOrder(String string1, String string2, String order) {

			if (order.equalsIgnoreCase("asc")) {
				return compareTo(string1, string2);
			} else {
				return -compareTo(string1, string2);
			}
		}

		private int compareTo(String string1, String string2) {
			if (string1 == null) {
				string1 = "";
			}
			if (string2 == null) {
				string2 = "";
			}

			Class valueTypeClass = sortingTemplate.getValueType();
			if (valueTypeClass != null) {

//				TODO implement set of supported classes
				if (valueTypeClass.equals(Calendar.class)) {
					return compareDates(string1, string2);
				}
			}
//			return string1.compareTo(string2);
			return AlphanumComparator.compare(string1, string2);
		}

		private int compareDates(String string1, String string2) {
			String conversionPattern = sortingTemplate.getConversionPattern();
			if (conversionPattern == null) {
				//TODO log errors
				return 0;
			}
			SimpleDateFormat format = new SimpleDateFormat(conversionPattern);
			try {
				Date d1 = format.parse(string1);
				Date d2 = format.parse(string2);
				return d1.compareTo(d2);
			} catch (ParseException e) {
				//TODO log errors
				return 0;
			}
		}
	}
}
