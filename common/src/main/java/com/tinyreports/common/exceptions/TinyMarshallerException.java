package com.tinyreports.common.exceptions;

/**
 * @author Anton Nesterenko
 * @since 0.5.4
 */
public class TinyMarshallerException extends TinyReportException {
    public TinyMarshallerException(String message, Object... parameters) {
        super(message, parameters);
    }

    public TinyMarshallerException(String message, Throwable cause, Object... parameters) {
        super(message, cause, parameters);
    }
}
