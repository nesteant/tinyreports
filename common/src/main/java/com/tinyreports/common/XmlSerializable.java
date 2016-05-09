package com.tinyreports.common;

import com.tinyreports.common.exceptions.TinyReportException;
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
 * @since 0.6
 */
public interface XmlSerializable<T> {
    void serialize(T object, Result result) throws TinyReportException;

    void serialize(T object, OutputStream os) throws TinyReportException;

    void serialize(T object, File output) throws TinyReportException;

    void serialize(T object, Writer writer) throws TinyReportException;

    void serialize(T object, ContentHandler handler) throws TinyReportException;

    void serialize(T object, Node node) throws TinyReportException;

    void serialize(T object, XMLStreamWriter writer) throws TinyReportException;

    void serialize(T object, XMLEventWriter writer) throws TinyReportException;
}
