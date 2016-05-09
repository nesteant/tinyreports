package com.tinyreports.common.exceptions;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class TinyReportInvalidParameterException extends TinyReportException {

	public TinyReportInvalidParameterException(String message, Object... parameters) {
		super(message, parameters);
	}

	public TinyReportInvalidParameterException(String message, Throwable cause, Object... parameters) {
		super(message, cause, parameters);
	}
}
