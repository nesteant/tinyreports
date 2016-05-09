package com.tinyreports.common.exceptions;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class TinyReportTemplateException extends TinyReportException {

	public TinyReportTemplateException(String message, Object... parameters) {
		super(message, parameters);
	}

	public TinyReportTemplateException(String message, Throwable cause, Object... parameters) {
		super(message, cause, parameters);
	}
}
