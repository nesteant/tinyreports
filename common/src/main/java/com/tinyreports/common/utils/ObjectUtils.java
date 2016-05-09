package com.tinyreports.common.utils;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class ObjectUtils {

	public static BlankObject createBlankObject() {

		return new BlankObject();
	}

	public static Boolean instanceOfBlankObject(Object o) {
		return o instanceof BlankObject;
	}

	private static class BlankObject {
	}

}
