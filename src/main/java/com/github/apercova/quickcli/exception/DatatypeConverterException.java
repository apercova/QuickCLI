package com.github.apercova.quickcli.exception;

/**
 * Datatype conversion exception.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class DatatypeConverterException extends Exception {

    private static final long serialVersionUID = 4772821535245558588L;

    public DatatypeConverterException() {
        super();
    }

    public DatatypeConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatatypeConverterException(String message) {
        super(message);
    }

    public DatatypeConverterException(Throwable cause) {
        super(cause);
    }

}
