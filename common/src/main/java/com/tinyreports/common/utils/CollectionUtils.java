package com.tinyreports.common.utils;

import java.util.Collection;

/**
 * @author Anton Nesterenko
 * @since 0.6
 */
public class CollectionUtils {

	public static boolean isNotEmpty(Collection collection) {
		return collection != null && collection.size() > 0;
	}

	public static boolean isEmpty(Collection collection) {
		return !isNotEmpty(collection);
	}
}
