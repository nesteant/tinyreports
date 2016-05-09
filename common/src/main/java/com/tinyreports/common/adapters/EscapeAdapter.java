package com.tinyreports.common.adapters;

import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class EscapeAdapter extends XmlAdapter<String, String> {
    @Override
    public String unmarshal(String v) throws Exception {
        return StringEscapeUtils.unescapeXml(v);
    }

    @Override
    public String marshal(String v) throws Exception {
        return StringEscapeUtils.escapeXml(v);
    }
}
