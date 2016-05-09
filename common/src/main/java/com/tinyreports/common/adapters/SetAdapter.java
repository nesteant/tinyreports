package com.tinyreports.common.adapters;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class SetAdapter extends XmlAdapter<String, Set<String>> {
    @Override
    public Set<String> unmarshal(String v) throws Exception {
        Set<String> arrayList = new HashSet<String>();
        if (StringUtils.isEmpty(v)) {
            return arrayList;
        }
        Collections.addAll(arrayList, v.replaceAll("\\s", "").split(","));
        return arrayList;
    }

    @Override
    public String marshal(Set<String> v) throws Exception {
        if (v == null) {
            return "";
        }
        return StringUtils.join(v.toArray(), ",");
    }
}
