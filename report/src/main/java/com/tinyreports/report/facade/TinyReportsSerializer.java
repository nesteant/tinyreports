package com.tinyreports.report.facade;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.XmlSerializer;
import com.tinyreports.report.models.transfer.GroupingReport;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Anton Nesterenko
 * @since 0.6.0
 */
public final class TinyReportsSerializer {

	public static void serialize(GroupingReport object, Result result) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, result);
	}

	public static void serialize(GroupingReport object, OutputStream os) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, os);
	}

	public static void serialize(GroupingReport object, File output) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, output);
	}

	public static void serialize(GroupingReport object, Writer writer) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, writer);
	}

	public static void serialize(GroupingReport object, ContentHandler handler) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, handler);
	}

	public static void serialize(GroupingReport object, Node node) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, node);
	}

	public static void serialize(GroupingReport object, XMLStreamWriter writer) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, writer);
	}

	public static void serialize(GroupingReport object, XMLEventWriter writer) throws TinyReportException {
		new XmlSerializer<GroupingReport>().serialize(object, writer);
	}
}
