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

import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class HtmlRowObject {

	private List<Element> trs;

	private Integer rowHeight;

	private Boolean odd;

	public void appendRow(Element element) {
		for (Element tr : trs) {
			element.appendChild(tr);
		}
	}

	public void insertTo(Element element) {
		for (Element tr : trs) {
			element.appendChild(tr);
		}
	}

	public List<Element> getTrs() {
		return trs;
	}

	public void setTrs(List<Element> trs) {
		this.trs = trs;
	}

	public Integer getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(Integer rowHeight) {
		this.rowHeight = rowHeight;
	}

	public boolean isOdd() {
		return odd;
	}

	public void setOdd(Boolean odd) {
		this.odd = odd;
	}
}
