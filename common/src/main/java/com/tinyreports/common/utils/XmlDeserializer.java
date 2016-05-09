package com.tinyreports.common.utils;

import com.tinyreports.common.XmlDeserializable;
import com.tinyreports.common.exceptions.TinyReportException;
import com.tinyreports.common.exceptions.TinyReportTemplateException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
@SuppressWarnings("unchecked")
public class XmlDeserializer<T> implements XmlDeserializable<T> {
    private static final String INVALID_FORMAT_TEXT = "Invalid format of input xml node";
    private Class<T> clazz;

    public XmlDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(File f) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(f);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(InputStream is) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(is);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(Reader reader) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(URL url) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(url);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(InputSource source) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(source);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(Node node) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(node);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public <T> JAXBElement<T> deserialize(Node node, Class<T> declaredType) throws TinyReportException {
        try {
            return getUnmarshaller(clazz).unmarshal(node, declaredType);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(Source source) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(source);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public <T> JAXBElement<T> deserialize(Source source, Class<T> declaredType) throws TinyReportException {
        try {
            return getUnmarshaller(clazz).unmarshal(source, declaredType);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(XMLStreamReader reader) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public <T> JAXBElement<T> deserialize(XMLStreamReader reader, Class<T> declaredType) throws TinyReportException {
        try {
            return getUnmarshaller(clazz).unmarshal(reader, declaredType);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public T deserialize(XMLEventReader reader) throws TinyReportException {
        try {
            return (T) getUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    @Override
    public <T> JAXBElement<T> deserialize(XMLEventReader reader, Class<T> declaredType) throws TinyReportException {
        try {
            return getUnmarshaller(clazz).unmarshal(reader, declaredType);
        } catch (JAXBException e) {
            throw new TinyReportException(INVALID_FORMAT_TEXT, e);
        }
    }

    public Unmarshaller getUnmarshaller(Class<T> clazz) throws TinyReportTemplateException {
        try {
            JAXBContext ctx = JAXBContext.newInstance(clazz);
            return ctx.createUnmarshaller();
        } catch (JAXBException jaxbException) {
            throw new TinyReportTemplateException(INVALID_FORMAT_TEXT, jaxbException);
        }
    }
}
