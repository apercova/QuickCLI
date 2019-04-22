package com.github.apercova.quickcli.exception;

/**
 * Represents a reflective operation exception that is intended to be thrown as
 * consequence of:
 * <br> null {@link java.lang.ClassNotFoundException ClassNotFoundException},
 * {@link java.lang.IllegalAccessException IllegalAccessException},
 * {@link java.lang.InstantiationException InstantiationException},
 * {@link java.lang.reflect.InvocationTargetException InvocationTargetException},
 * {@link java.lang.NoSuchFieldException NoSuchFieldException},
 * {@link java.lang.NoSuchMethodException NoSuchMethodException}
 * <br><br>
 * As of JavaSE 7 onwards, use
 * {@link java.lang.ReflectiveOperationException ReflectiveOperationException}
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class ReflectiveOperationException extends Exception {

    private static final long serialVersionUID = 3453914816515255045L;

    public ReflectiveOperationException() {
        super();
    }

    public ReflectiveOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectiveOperationException(String message) {
        super(message);
    }

    public ReflectiveOperationException(Throwable cause) {
        super(cause);
    }
}
