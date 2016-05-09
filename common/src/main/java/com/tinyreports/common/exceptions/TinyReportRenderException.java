package com.tinyreports.common.exceptions;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class TinyReportRenderException extends TinyReportException {

	public TinyReportRenderException() {
	}

	public TinyReportRenderException(String message) {
		super(message);
	}

	public TinyReportRenderException(Throwable cause) {
		super(cause);
	}

	public TinyReportRenderException(String message, Object... parameters) {
		super(message, parameters);
	}

	public TinyReportRenderException(String message, Throwable cause, Object... parameters) {
		super(message, cause, parameters);
	}
}
