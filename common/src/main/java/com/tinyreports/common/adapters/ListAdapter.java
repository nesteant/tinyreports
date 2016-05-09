package com.tinyreports.common.adapters;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ListAdapter extends XmlAdapter<String, List<String>> {
	@Override
	public List<String> unmarshal(String v) throws Exception {
		List<String> arrayList = new ArrayList<String>();
		if (StringUtils.isEmpty(v)) {
			return arrayList;
		}

		Collections.addAll(arrayList, v.replaceAll("\\s", "").split(","));
		return arrayList;
	}

	@Override
	public String marshal(List<String> v) throws Exception {
		if (v == null) {
			return "";
		}
		return StringUtils.join(v.toArray(), ",");
	}

}
