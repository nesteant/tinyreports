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

package com.tinyreports.common;

import com.tinyreports.common.exceptions.TinyMarshallerException;
import com.tinyreports.common.utils.DomOperations;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.CustomizedTagBalancer;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author Anton Nesterenko
 */
public class Test {

	public static void main(String[] args) throws SAXException, IOException, TinyMarshallerException {
		DOMParser parser = new DOMParser();
		parser.setFeature("http://cyberneko.org/html/features/balance-tags", false);
		XMLDocumentFilter[] filters = {new CustomizedTagBalancer()};
		parser.setProperty("http://cyberneko.org/html/properties/filters", filters);

		parser.parse(new InputSource(new FileReader("/build/projects/tinyreports/report/src/test/resources/com/tinyreports/report/layout.html")));
		String str = DomOperations.toString(parser.getDocument());
		String x = "";
	}
}
