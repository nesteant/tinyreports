package com.tinyreports.common.exceptions;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class TinyReportException extends Exception {

	public TinyReportException() {
	}

	public TinyReportException(String message) {
		super(message);
	}

	public TinyReportException(Throwable cause) {
		super(cause);
	}

	public TinyReportException(String message, Object... parameters) {
		super(String.format(message, parameters));
	}

	public TinyReportException(String message, Throwable cause, Object... parameters) {
		super(String.format(message, parameters), cause);
	}

}
