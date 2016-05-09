package com.tinyreports.common.exceptions;

/**
 * @author Anton Nesterenko
 * @since 0.5.3
 */
public class StreamingReportException extends TinyReportException {
    public StreamingReportException(String message, Object... parameters) {
        super(message, parameters);
    }

    public StreamingReportException(String message, Throwable cause, Object... parameters) {
        super(message, cause, parameters);
    }
}
