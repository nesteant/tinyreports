package com.tinyreports.common.utils;

import com.tinyreports.common.XmlSerializable;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class XmlSerializer<T> implements XmlSerializable<T> {

	private static final String INVALID_FORMAT_TEXT = "Invalid format of input xml node";

	@Override
	public void serialize(T object, Result result) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, result);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, OutputStream os) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, os);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, File output) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, output);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, Writer writer) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, writer);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, ContentHandler handler) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, handler);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, Node node) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, node);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, XMLStreamWriter writer) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, writer);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	@Override
	public void serialize(T object, XMLEventWriter writer) throws TinyReportException {
		try {
			getMarshaller(object).marshal(object, writer);
		} catch (JAXBException e) {
			throw new TinyReportException(INVALID_FORMAT_TEXT, e);
		}
	}

	private javax.xml.bind.Marshaller getMarshaller(T object) throws TinyReportTemplateException {
		try {
			JAXBContext ctx = JAXBContext.newInstance(object.getClass());

			javax.xml.bind.Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
			return marshaller;
		} catch (JAXBException jaxbException) {
			throw new TinyReportTemplateException(INVALID_FORMAT_TEXT, jaxbException);
		}
	}
}
