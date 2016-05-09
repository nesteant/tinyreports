package com.tinyreports.common.utils;

import com.tinyreports.common.exceptions.TinyMarshallerException;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.cyberneko.html.CustomizedTagBalancer;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public final class DomOperations {

	private static TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

	public static Document parseHtml(InputSource inputSource) throws TinyMarshallerException {
		try {
			DOMParser parser = getHParser();
			parser.parse(inputSource);
			return parser.getDocument();
		} catch (SAXException e) {
			throw new TinyMarshallerException("Error while parsing InputSource", e);
		} catch (IOException e) {
			throw new TinyMarshallerException("Error while reading InputSource", e);
		}
	}

	public static Document parseBHtml(InputSource inputSource) throws TinyMarshallerException {
		try {
			DOMParser parser = getBHParser();
			parser.parse(inputSource);
			return parser.getDocument();
		} catch (SAXException e) {
			throw new TinyMarshallerException("Error while parsing InputSource", e);
		} catch (IOException e) {
			throw new TinyMarshallerException("Error while reading InputSource", e);
		}
	}

	public static Document parseXml(InputSource inputSource) throws TinyMarshallerException {
		Document document;
		try {
			DOMParser parser = getXmlParser();
			parser.parse(inputSource);
			document = parser.getDocument();
		} catch (SAXException e) {
			throw new TinyMarshallerException("Error while parsing InputSource", e);
		} catch (IOException e) {
			throw new TinyMarshallerException("Error while reading InputSource", e);
		}
		return document;
	}


	public static Document newXmlDoc() throws TinyMarshallerException {
		Document doc = parseXml(new InputSource(new StringReader("<root/>")));
		Node n = doc.getDocumentElement();
		doc.removeChild(n);
		return doc;
	}


	public static String toString(NodeList nodeList) throws TinyMarshallerException {
		StringWriter wr = new StringWriter();
		for (int i = 0; i < nodeList.getLength(); i++) {
			wr.append(toString(nodeList.item(i)));
		}
		return wr.toString();
	}

	public static String toString(Node node) throws TinyMarshallerException {

		Transformer transformer = getTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "US-ASCII");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		node.normalize();

		DOMSource source = new DOMSource(node);
		transform(transformer, source, result);
		return sw.toString().replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "");
	}

	public static List<Element> toList(NodeList nodeList) {
		List<Element> list = new ArrayList<Element>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			list.add((Element) nodeList.item(i));
		}
		return list;
	}

	public static Document toHtmlDocument(String str) throws TinyMarshallerException {
		InputSource is = new InputSource(new StringReader(str));
		return parseHtml(is);
	}

	public static Node toHtmlNode(String str, Document document) throws TinyMarshallerException {
		InputSource is = new InputSource(new StringReader(str));
		Element documentElement = parseHtml(is).getDocumentElement();
		if (documentElement == null) {
			return null;
		}
		return document.importNode(documentElement, true);
	}

	public static Node toHtmlNode(String str) throws TinyMarshallerException {
		InputSource is = new InputSource(new StringReader(str));
		return parseHtml(is).getDocumentElement();
	}

	private static Transformer getTransformer() throws TinyMarshallerException {
		try {
			return TRANSFORMER_FACTORY.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new TinyMarshallerException("Error creating transformer", e);
		}
	}

	private static void transform(Transformer transformer, Source xmlSource, Result outputTarget) throws TinyMarshallerException {
		try {
			transformer.transform(xmlSource, outputTarget);
		} catch (TransformerException e) {
			throw new TinyMarshallerException("Error during transformation", e);
		}
	}

	public static DOMParser getBHParser() {
		XMLParserConfiguration xmlConfig = new HTMLConfiguration();
		XMLDocumentFilter[] filters = {new CustomizedTagBalancer()};
//		XMLDocumentFilter[] filters = { new HtmlWriter(writer, encoding)};
//		xmlConfig.setProperty("http://cyberneko.org/html/properties/filters", filters);
		xmlConfig.setFeature("http://cyberneko.org/html/features/augmentations", true);
		xmlConfig.setProperty("http://cyberneko.org/html/properties/names/elems", "match");
		xmlConfig.setProperty("http://cyberneko.org/html/properties/names/attrs", "match");
		xmlConfig.setFeature("http://cyberneko.org/html/features/balance-tags", false);
		xmlConfig.setProperty("http://cyberneko.org/html/properties/filters", filters);
		xmlConfig.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);
		xmlConfig.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
		xmlConfig.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
		xmlConfig.setFeature("http://cyberneko.org/html/features/scanner/cdata-sections", true);
		return new DOMParser(xmlConfig);

	}

	public static DOMParser getHParser() throws TinyMarshallerException {
		XMLParserConfiguration xmlConfig = new HTMLConfiguration();
		XMLDocumentFilter[] filters = {new CustomizedTagBalancer()};
//		XMLDocumentFilter[] filters = { new HtmlWriter(writer, encoding)};
//		xmlConfig.setProperty("http://cyberneko.org/html/properties/filters", filters);
		xmlConfig.setFeature("http://cyberneko.org/html/features/augmentations", true);
		xmlConfig.setProperty("http://cyberneko.org/html/properties/names/elems", "match");
		xmlConfig.setProperty("http://cyberneko.org/html/properties/names/attrs", "match");
		xmlConfig.setFeature("http://cyberneko.org/html/features/balance-tags", false);
		xmlConfig.setProperty("http://cyberneko.org/html/properties/filters", filters);
		xmlConfig.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);
		xmlConfig.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
		xmlConfig.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
		xmlConfig.setFeature("http://cyberneko.org/html/features/scanner/cdata-sections", true);
		DOMParser domParser = new DOMParser(xmlConfig);
		try {
			domParser.setFeature("http://xml.org/sax/features/namespaces", true);
			return domParser;
		} catch (SAXNotRecognizedException e) {
			throw new TinyMarshallerException("Error configuring parser", e);
		} catch (SAXNotSupportedException e) {
			throw new TinyMarshallerException("Error configuring parser", e);
		}
	}

	private static DOMParser getXmlParser() throws TinyMarshallerException {
		DOMParser parser = new DOMParser();
		try {
			parser.setFeature("http://xml.org/sax/features/namespaces", true);
			return parser;
		} catch (SAXNotRecognizedException e) {
			throw new TinyMarshallerException("Error configuring parser", e);
		} catch (SAXNotSupportedException e) {
			throw new TinyMarshallerException("Error configuring parser", e);
		}

	}

	public static Node findChildByLocalName(Node node, String localName) {
		NodeList list = node.getChildNodes();
		if (list == null) {
			return null;
		}

		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n.getLocalName() != null && n.getLocalName().equalsIgnoreCase(localName)) {
				return n;
			}
		}
		return null;
	}
}
