package com.tinyreports.common.adapters;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ArrayAdapter extends XmlAdapter<String, String[]> {
    @Override
    public String[] unmarshal(String v) throws Exception {
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        return v.replaceAll("\\s", "").split(",");
    }

    @Override
    public String marshal(String[] v) throws Exception {
        return StringUtils.join(v);
    }
}
