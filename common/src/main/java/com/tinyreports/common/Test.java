package com.tinyreports.common;

import com.tinyreports.common.exceptions.TinyMarshallerException;
import com.tinyreports.common.utils.DomOperations;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.CustomizedTagBalancer;
import org.cyberneko.html.parsers.DOMParser;
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
