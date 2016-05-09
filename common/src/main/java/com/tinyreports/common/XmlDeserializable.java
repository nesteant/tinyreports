package com.tinyreports.common;

import com.tinyreports.common.exceptions.TinyReportException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBElement;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public interface XmlDeserializable<T> {

	T deserialize(File f) throws TinyReportException;

	T deserialize(InputStream is) throws TinyReportException;

	T deserialize(Reader reader) throws TinyReportException;

	T deserialize(URL url) throws TinyReportException;

	T deserialize(InputSource source) throws TinyReportException;

	T deserialize(Node node) throws TinyReportException;

	<T> JAXBElement<T> deserialize(Node node, Class<T> declaredType) throws TinyReportException;

	T deserialize(Source source) throws TinyReportException;

	<T> JAXBElement<T> deserialize(Source source, Class<T> declaredType) throws TinyReportException;

	T deserialize(XMLStreamReader reader) throws TinyReportException;

	<T> JAXBElement<T> deserialize(XMLStreamReader reader, Class<T> declaredType) throws TinyReportException;

	T deserialize(XMLEventReader reader) throws TinyReportException;

	<T> JAXBElement<T> deserialize(XMLEventReader reader, Class<T> declaredType) throws TinyReportException;
}
