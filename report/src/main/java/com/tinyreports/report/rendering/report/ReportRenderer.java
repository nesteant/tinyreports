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

import com.tinyreports.common.exceptions.StreamingReportException;
import com.tinyreports.common.exceptions.TinyMarshallerException;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.CollectionUtils;
import com.tinyreports.common.utils.DomOperations;
import com.tinyreports.report.models.templates.CaptionTemplate;
import com.tinyreports.report.models.templates.ColumnTemplate;
import com.tinyreports.report.models.templates.HtmlTemplate;
import com.tinyreports.report.models.templates.ReportTemplate;
import com.tinyreports.report.models.templates.ValueTemplate;
import com.tinyreports.report.models.transfer.XmlCell;
import com.tinyreports.report.models.transfer.XmlHeader;
import com.tinyreports.report.models.transfer.XmlReport;
import com.tinyreports.report.models.transfer.XmlRow;
import com.tinyreports.report.models.transfer.csv.CsvReport;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ReportRenderer {

	private static final String STREAMING_EXCEPTION_TEXT = "Error during writing to passed stream";

	private XmlReport xmlReport;
	private Document document;

	public ReportRenderer(XmlReport xmlReport) throws TinyMarshallerException {
		this.xmlReport = xmlReport;
		document = DomOperations.newXmlDoc();
	}

	public <E> E render(Class<E> renderResultType) throws TinyReportException {

		ReportTemplate template = xmlReport.getReportTemplate();
		List<XmlRow> xmlRowList = xmlReport.getXmlRows();

		List<String> csvRows = new ArrayList<String>();

		if (renderResultType.equals(CsvReport.class)) {
			CsvReport csvReport = new CsvReport();
			csvReport.setName(renderCaption());
			List<HtmlRowObject> htmlRowObjects = new ArrayList<HtmlRowObject>();

			List<String> headerRow = new ArrayList<String>();
			for (XmlHeader xmlHeader : xmlReport.getXmlHeaders()) {
				if (xmlHeader.getHidden() != null && xmlHeader.getHidden()) {
					continue;
				}
				headerRow.add(StringEscapeUtils.escapeCsv(xmlHeader.getValue().trim()));
			}
			String csvSeparator = ",";
			csvRows.add(StringUtils.join(headerRow, csvSeparator));

			if (CollectionUtils.isNotEmpty(xmlRowList)) {
				HtmlReportSortingService.provideOuterSorting(xmlReport);
				boolean isOdd = false;
				for (XmlRow xmlRow : xmlRowList) {

					HtmlReportSortingService.provideInnerSorting(xmlRow, xmlReport.getReportTemplate());

					xmlRow.prepareCellMap();
					HeightNormalizer.normalizeCellHeights(xmlRow.getChildCellMap(), xmlReport.getReportTemplate());

					HtmlRowObject htmlRowObject = generateHtmlToCsvRow(isOdd, xmlRow, template);
					isOdd = !htmlRowObject.isOdd();
					htmlRowObjects.add(htmlRowObject);
				}
			}

			List<XmlRow> xmlAggregations = xmlReport.getXmlAggregations();
			if (CollectionUtils.isNotEmpty(xmlAggregations)) {
				for (XmlRow xmlAggregation : xmlAggregations) {
					xmlAggregation.prepareCellMap();
					HeightNormalizer.normalizeCellHeights(xmlAggregation.getChildCellMap(), template);
					HtmlRowObject aggregationHtmlRow = generateHtmlToCsvRow(null, xmlAggregation, template);
					htmlRowObjects.add(aggregationHtmlRow);
				}
			}
			concatenateReportDocument(htmlRowObjects);

			for (HtmlRowObject htmlRowObject : htmlRowObjects) {
				List<Element> trs = htmlRowObject.getTrs();

				for (Element tr : trs) {
					List<String> csvRow = new ArrayList<String>();
					NodeList childNodes = tr.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						Node cn = childNodes.item(i);
						String textContent = cn.getTextContent();
						if (textContent != null) {
							textContent = StringEscapeUtils.escapeCsv(textContent.trim());
							csvRow.add(textContent);
						}
					}
					String joinedRow = StringUtils.join(csvRow, csvSeparator);
					csvRows.add(joinedRow);
				}
			}
			csvReport.setRows(csvRows);
			return (E) csvReport;
		}

		List<HtmlRowObject> htmlRowObjects = new ArrayList<HtmlRowObject>();

		if (CollectionUtils.isNotEmpty(xmlRowList)) {
			HtmlReportSortingService.provideOuterSorting(xmlReport);
			boolean isOdd = false;
			for (XmlRow xmlRow : xmlRowList) {

				HtmlReportSortingService.provideInnerSorting(xmlRow, xmlReport.getReportTemplate());

				xmlRow.prepareCellMap();
				HeightNormalizer.normalizeCellHeights(xmlRow.getChildCellMap(), xmlReport.getReportTemplate());

				HtmlRowObject htmlRowObject = generateRow(isOdd, xmlRow, template);
				isOdd = !htmlRowObject.isOdd();
				htmlRowObjects.add(htmlRowObject);
			}
		}

		List<XmlRow> xmlAggregations = xmlReport.getXmlAggregations();
		if (CollectionUtils.isNotEmpty(xmlAggregations)) {
			for (XmlRow xmlAggregation : xmlAggregations) {
				xmlAggregation.prepareCellMap();
				HeightNormalizer.normalizeCellHeights(xmlAggregation.getChildCellMap(), template);
				HtmlRowObject aggregationHtmlRow = generateRow(null, xmlAggregation, template);
				htmlRowObjects.add(aggregationHtmlRow);
			}
		}

		if (xmlReport.getBlank()) {
			concatenateReportDocument(new ArrayList<HtmlRowObject>() {{
				add(createBlankRow());
			}});
		} else {
			concatenateReportDocument(htmlRowObjects);
		}
		if (renderResultType.equals(Document.class)) {
			return (E) document;
		} else if (renderResultType.equals(String.class)) {
			return (E) DomOperations.toString(document);
		} else {
			throw new IllegalStateException(String.format("%s type is not supported", renderResultType.getName()));
		}
	}

	public void render(Writer writer) throws TinyReportException {
		String result = render(String.class);
		try {
			IOUtils.write(result, writer);
		} catch (IOException e) {
			throw new StreamingReportException(STREAMING_EXCEPTION_TEXT, e);
		}
	}

	public void render(OutputStream outputStream) throws TinyReportException {
		String result = render(String.class);

		try {
			IOUtils.write(result, outputStream);
		} catch (IOException e) {
			throw new StreamingReportException(STREAMING_EXCEPTION_TEXT, e);
		}
	}

	private HtmlRowObject createBlankRow() {
		HtmlRowObject htmlRowObject = new HtmlRowObject();
		List<Element> trs = new ArrayList<Element>();
		Element tr = document.createElement("tr");
		List<ColumnTemplate> columns = xmlReport.getReportTemplate().getColumns();
		int colspan = columns.size();
		for (ColumnTemplate column : columns) {
			if (column.getHidden() != null && column.getHidden()) {
				colspan--;
			}
		}
		Element td = document.createElement("td");
		td.setAttribute("colspan", String.valueOf(colspan));
		td.setAttribute("class", "blank_text");
		td.setTextContent(xmlReport.getBlankText());
		tr.appendChild(td);
		trs.add(tr);
		htmlRowObject.setTrs(trs);
		return htmlRowObject;
	}

	private HtmlRowObject generateHtmlToCsvRow(Boolean isOdd, XmlRow xmlRow, ReportTemplate template) throws TinyReportException {

		HtmlRowObject htmlRowObject = createHtmlRowObject(document, xmlRow);

		Integer columnNumber = 0;


		List<ColumnTemplate> columnTemplates = template.getColumns();
		List<XmlCell> firstRowCells = xmlRow.getChildCellMap().get(columnTemplates.get(0).getId());
		for (XmlCell firstRowCell : firstRowCells) {
			firstRowCell.setFirst(true);
		}

		for (ColumnTemplate columnTemplate : columnTemplates) {
			if (columnTemplate.getHidden() != null && columnTemplate.getHidden()) {
				continue;
			}
			String columnId = columnTemplate.getId();

			List<XmlCell> currentColumnCellImpls = xmlRow.getChildCellMap().get(columnId);
			Integer innerRowPointer = 0;

			for (XmlCell currentColumnCellImpl : currentColumnCellImpls) {

				Element currentRowElement = htmlRowObject.getTrs().get(innerRowPointer);

				Integer currentCellHeight = currentColumnCellImpl.getCellHeight();
				Element htmlCell = document.createElement("td");

				if (currentColumnCellImpl.isFirst()) {
					markRowWithStyleClass(currentRowElement, "frow");
					isOdd = invertOdd(isOdd);
				}

				appendOddClass(currentRowElement, isOdd);
				if (currentCellHeight > 1) {
//					htmlCell.setAttribute("rowspan", currentCellHeight.toString());
					for (int i = 1; i < currentCellHeight; i++) {
						Element fakeNode = document.createElement("td");
						generateCellContent(fakeNode, currentColumnCellImpl, columnTemplate);
						Element row = htmlRowObject.getTrs().get(innerRowPointer + i);
						appendOddClass(row, isOdd);
						row.appendChild(fakeNode);
					}
				}

				Integer countOfPreviousCells = currentRowElement.getChildNodes().getLength();
				Integer lackOfNodes = columnNumber - countOfPreviousCells;
				for (int i = 0; i < lackOfNodes; i++) {
					Element substituteNode = document.createElement("td");
					substituteNode.setAttribute("substitute", "true");
					currentRowElement.appendChild(substituteNode);
				}

				generateCellContent(htmlCell, currentColumnCellImpl, columnTemplate);

				//TODO !!!!!!!!!!!!!!!!!!!!!
				currentColumnCellImpl.copyAttributesToElement(htmlCell);

				currentRowElement.appendChild(htmlCell);
				innerRowPointer += currentCellHeight;
			}
			columnNumber++;
		}
		isOdd = invertOdd(isOdd);
		htmlRowObject.setOdd(isOdd);
		return htmlRowObject;
	}

	private void generateCellContent(Element htmlCell, XmlCell currentColumnCell, ColumnTemplate columnTemplate) throws TinyMarshallerException {
		String content = currentColumnCell.getContent();
		Boolean isTagContent = currentColumnCell.getTagContent();
		if (content == null || content.isEmpty()) {
			htmlCell.setTextContent("");
		} else if (isTagContent != null && isTagContent) {
			ValueTemplate valueTemplate = columnTemplate.getValueTemplate();
			List<String> escapeTags = valueTemplate.getEscapeTags();
			if (escapeTags != null) {
				for (String escapeTag : escapeTags) {
					Pattern patern = Pattern.compile("(<)([/]?" + escapeTag + "[\\s\\w\\W]*?)(>)");
					Matcher matcher = patern.matcher(content);
					StringBuffer sb = new StringBuffer();
					while (matcher.find()) {
						String lt = "&lt;";
						String gt = "&gt;";
						matcher.appendReplacement(sb, lt + matcher.group(2) + gt);
					}
					matcher.appendTail(sb);
					content = sb.toString();
				}
			}

			List<String> tagFilters = valueTemplate.getTagFilters();
			if (escapeTags != null) {
				for (String tagFilter : tagFilters) {
					Pattern patern = Pattern.compile("(<)([/]?" + tagFilter + "[\\s\\w\\W]*?)(>)");
					Matcher matcher = patern.matcher(content);
					StringBuffer sb = new StringBuffer();
					while (matcher.find()) {
						matcher.appendReplacement(sb, "");
					}
					matcher.appendTail(sb);
					content = sb.toString();
				}
			}

			Node cellContent = DomOperations.toHtmlNode(content, document);
			if (cellContent != null) {
				htmlCell.appendChild(cellContent);
			}
			if (!content.isEmpty() && cellContent == null) {
				htmlCell.setTextContent(content);
			}
		} else {
			htmlCell.setTextContent(content);
		}
	}

	private HtmlRowObject generateRow(Boolean isOdd, XmlRow xmlRow, ReportTemplate template) throws TinyReportException {

		HtmlRowObject htmlRowObject = createHtmlRowObject(document, xmlRow);

		Integer columnNumber = 0;


		List<ColumnTemplate> columnTemplates = template.getColumns();
		List<XmlCell> firstRowCells = xmlRow.getChildCellMap().get(columnTemplates.get(0).getId());
		for (XmlCell firstRowCell : firstRowCells) {
			firstRowCell.setFirst(true);
		}

		for (ColumnTemplate columnTemplate : columnTemplates) {
			if (columnTemplate.getHidden() != null && columnTemplate.getHidden()) {
				continue;
			}
			String columnId = columnTemplate.getId();

			List<XmlCell> currentColumnCellImpls = xmlRow.getChildCellMap().get(columnId);
			Integer innerRowPointer = 0;

			for (XmlCell currentColumnCellImpl : currentColumnCellImpls) {

				Element currentRowElement = htmlRowObject.getTrs().get(innerRowPointer);

				Integer currentCellHeight = currentColumnCellImpl.getCellHeight();
				Element htmlCell = document.createElement("td");

				if (currentColumnCellImpl.isFirst()) {
					markRowWithStyleClass(currentRowElement, "frow");
					isOdd = invertOdd(isOdd);
				}

				appendOddClass(currentRowElement, isOdd);
				if (currentCellHeight > 1) {
//					htmlCell.setAttribute("rowspan", currentCellHeight.toString());
					for (int i = 1; i < currentCellHeight; i++) {
						Element fakeNode = document.createElement("td");
//						fakeNode.setAttribute("fake", "true");
						Element row = htmlRowObject.getTrs().get(innerRowPointer + i);
						appendOddClass(row, isOdd);
						row.appendChild(fakeNode);
					}
				}

				Integer countOfPreviousCells = currentRowElement.getChildNodes().getLength();
				Integer lackOfNodes = columnNumber - countOfPreviousCells;
				for (int i = 0; i < lackOfNodes; i++) {
					Element substituteNode = document.createElement("td");
					substituteNode.setAttribute("substitute", "true");
					currentRowElement.appendChild(substituteNode);
				}


				String content = currentColumnCellImpl.getContent();
				Boolean isTagContent = currentColumnCellImpl.getTagContent();
				if (content == null || content.isEmpty()) {
					htmlCell.setTextContent("");
				} else if (isTagContent != null && isTagContent) {
					ValueTemplate valueTemplate = columnTemplate.getValueTemplate();
					List<String> escapeTags = valueTemplate.getEscapeTags();
					if (escapeTags != null) {
						for (String escapeTag : escapeTags) {
							Pattern patern = Pattern.compile("(<)([/]?" + escapeTag + "[\\s\\w\\W]*?)(>)");
							Matcher matcher = patern.matcher(content);
							StringBuffer sb = new StringBuffer();
							while (matcher.find()) {
								String lt = "&lt;";
								String gt = "&gt;";
								matcher.appendReplacement(sb, lt + matcher.group(2) + gt);
							}
							matcher.appendTail(sb);
							content = sb.toString();
						}
					}

					List<String> tagFilters = valueTemplate.getTagFilters();
					if (escapeTags != null) {
						for (String tagFilter : tagFilters) {
							Pattern patern = Pattern.compile("(<)([/]?" + tagFilter + "[\\s\\w\\W]*?)(>)");
							Matcher matcher = patern.matcher(content);
							StringBuffer sb = new StringBuffer();
							while (matcher.find()) {
								matcher.appendReplacement(sb, "");
							}
							matcher.appendTail(sb);
							content = sb.toString();
						}
					}

					Node cellContent = DomOperations.toHtmlNode(content, document);
					if (cellContent != null) {
						htmlCell.appendChild(cellContent);
					}
					if (!content.isEmpty() && cellContent == null) {
						htmlCell.setTextContent(content);
					}
				} else {
					htmlCell.setTextContent(content);
				}

				//TODO !!!!!!!!!!!!!!!!!!!!!
				currentColumnCellImpl.copyAttributesToElement(htmlCell);

				currentRowElement.appendChild(htmlCell);
				innerRowPointer += currentCellHeight;
			}
			columnNumber++;
		}
		isOdd = invertOdd(isOdd);
		htmlRowObject.setOdd(isOdd);
		return htmlRowObject;
	}

	private Boolean invertOdd(Boolean odd) {
		if (odd != null) {
			return !odd;
		}
		return null;
	}

	private void markRowWithStyleClass(Element firstRow, String styleClass) {
		if (firstRow.hasAttribute("class")) {
			String lastRowAttribute = firstRow.getAttribute("class");
			if (!lastRowAttribute.contains(styleClass)) {
				firstRow.setAttribute("class", lastRowAttribute.concat(" " + styleClass));
			}
		} else {
			firstRow.setAttribute("class", styleClass);
		}
	}

	private void appendOddClass(Element el, Boolean isOdd) {
		if (isOdd == null) {
			return;
		}

		String aClass = "class";
		String odd = "row-odd";
		String even = "row-even";
		String currentClass;
		if (isOdd) {
			currentClass = odd;
		} else {
			currentClass = even;
		}
		if (el.hasAttribute(aClass)) {
			if (!el.getAttribute(aClass).contains(even) && !el.getAttribute(aClass).contains(odd)) {
				el.setAttribute(aClass, el.getAttribute(aClass).concat(" " + currentClass));
			}
		} else {
			el.setAttribute(aClass, currentClass);
		}
	}

	private void concatenateReportDocument(Collection<HtmlRowObject> htmlRows) throws TinyReportException {

		Element table = document.createElement("table");
		document.appendChild(table);

		Element captionElement = document.createElement("caption");
		table.appendChild(captionElement);

		Element headerThead = document.createElement("thead");
		Element headerHolder = document.createElement("tr");
		table.appendChild(headerThead);
		headerThead.appendChild(headerHolder);

		Element tbody = document.createElement("tbody");
		table.appendChild(tbody);

//		//TODO !!!!!!!!!!!!!!!!
		xmlReport.copyAttributesToElement(table);
		ReportTemplate template = xmlReport.getReportTemplate();

		CaptionTemplate captionTemplate = template.getCaption();
		Boolean displayCaption = captionTemplate.getDisplayCaption();
		HtmlTemplate htmlTemplate = captionTemplate.getCaptionContent();

		if (displayCaption == null || displayCaption) {

			if (htmlTemplate != null && StringUtils.isNotEmpty(htmlTemplate.getHtml())) {
				renderCaption(captionElement, true);
			} else {
				renderCaption(captionElement, false);
			}
		}

		if (captionTemplate.getDisplayHeader() == null || captionTemplate.getDisplayHeader()) {
			renderHeader(headerHolder);
		}

		for (HtmlRowObject htmlRow : htmlRows) {
			htmlRow.appendRow(tbody);
		}

		cleanupDocument();
		if (CollectionUtils.isNotEmpty(htmlRows)) {
			Element lastRow = (Element) tbody.getLastChild();
			markRowWithStyleClass(lastRow, "lrow");
		}
	}

	private String renderCaption() throws TinyReportException {
		String alternativeCaption = xmlReport.getXmlCaption().getAlternativeCaption();
		String captionString = xmlReport.getXmlCaption().getCaptionValue();

		String currentString;
		if (alternativeCaption != null) {
			currentString = alternativeCaption;
		} else {
			currentString = captionString;
		}
		return currentString;
	}

	private void renderCaption(Element htmlCaption, boolean isHtml) throws TinyReportException {
		String alternativeCaption = xmlReport.getXmlCaption().getAlternativeCaption();
		String captionString = xmlReport.getXmlCaption().getCaptionValue();

		String currentString;
		if (alternativeCaption != null) {
			currentString = alternativeCaption;
		} else {
			currentString = captionString;
		}

		if (isHtml) {
			htmlCaption.appendChild(DomOperations.toHtmlNode(currentString, document));
		} else {
			htmlCaption.setTextContent(currentString);
		}
	}

	private void renderHeader(Element htmlHeader) {
		Queue<XmlHeader> xmlHeaders = xmlReport.getXmlHeaders();
		for (XmlHeader xmlHeader : xmlHeaders) {
			if (xmlHeader.getHidden() != null && xmlHeader.getHidden()) {
				continue;
			}
			Element headerColumn = document.createElement("th");
			headerColumn.setTextContent(xmlHeader.getValue());
			htmlHeader.appendChild(headerColumn);
			//TODO !!!!!!!!!!!!!!!!
			xmlHeader.copyAttributesToElement(headerColumn);
		}
	}

	//TODO check performance
	private void cleanupDocument() {
		List<Node> nodes = new ArrayList<Node>();

		NodeList tds = document.getElementsByTagName("td");
		int count = tds.getLength();

		for (int i = 0; i < count; i++) {
			Element node = (Element) tds.item(i);
			if (node.hasAttribute("fake")) {
				nodes.add(node);
			}
		}

		for (Node node : nodes) {
			Node parent = node.getParentNode();
			parent.removeChild(node);
		}
	}

	private HtmlRowObject createHtmlRowObject(Document document, XmlRow xmlRow) {
		List<Element> trsHolder = new ArrayList<Element>();
		HtmlRowObject rowObject = new HtmlRowObject();

		for (int i = 0; i < xmlRow.getMaxHeight(); i++) {
			Element row = document.createElement("tr");
			//TODO !!!!!!!!!!!!!!!!
			xmlRow.copyAttributesToElement(row);
			trsHolder.add(row);
		}
		rowObject.setTrs(trsHolder);
		rowObject.setRowHeight(trsHolder.size());
		return rowObject;
	}
}
