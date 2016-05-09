package com.tinyreports.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ArrayUtils {


	public static List<Object> linearalizeArrayOfObjects(Object ... objects) {

		List<Object> finalList = new ArrayList<Object>();

		for (Object object : objects) {
			List<Object> list;
			if (object instanceof Collection) {
				list = new ArrayList((Collection) object);
			} else if (object.getClass().isArray()) {
				Object[] array = (Object[]) object;
				list = new ArrayList(array.length);
				Collections.addAll(list, array);
			} else {
				list = new ArrayList<Object>(1);
				list.add(object);
			}
			finalList.addAll(list);
		}
		return finalList;
	}
}
