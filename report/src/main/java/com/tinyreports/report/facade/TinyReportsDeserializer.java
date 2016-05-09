package com.tinyreports.report.facade;

import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.utils.XmlDeserializer;
import com.tinyreports.report.models.transfer.GroupingReport;
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
 * @since 2.0
 */
public final class TinyReportsDeserializer {
    public static GroupingReport deserialize(File f) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(f);
    }

    public static GroupingReport deserialize(InputStream is) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(is);
    }

    public static GroupingReport deserialize(Reader reader) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(reader);
    }

    public static GroupingReport deserialize(URL url) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(url);
    }

    public static GroupingReport deserialize(InputSource source) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(source);
    }

    public static GroupingReport deserialize(Node node) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(node);
    }

    public static <T> JAXBElement<T> deserialize(Node node, Class<T> declaredType) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(node, declaredType);
    }

    public static GroupingReport deserialize(Source source) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(source);
    }

    public static <T> JAXBElement<T> deserialize(Source source, Class<T> declaredType) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(source, declaredType);
    }

    public static GroupingReport deserialize(XMLStreamReader reader) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(reader);
    }

    public static <T> JAXBElement<T> deserialize(XMLStreamReader reader, Class<T> declaredType) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(reader, declaredType);
    }

    public static GroupingReport deserialize(XMLEventReader reader) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(reader);
    }

    public static <T> JAXBElement<T> deserialize(XMLEventReader reader, Class<T> declaredType) throws TinyReportException {
        return new XmlDeserializer<GroupingReport>(GroupingReport.class).deserialize(reader, declaredType);
    }
}
