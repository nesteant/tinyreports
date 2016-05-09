package com.tinyreports.common.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class CDataAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal(String v) throws Exception {
        return "<![CDATA[" + v + "]]>";
    }

    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }
}
