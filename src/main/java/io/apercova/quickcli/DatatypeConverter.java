package io.apercova.quickcli;

import io.apercova.quickcli.exception.DatatypeConverterException;

/**
 * Datatype converter.
 *
 * @param <T> Conversion destination type.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface DatatypeConverter<T> {

    /**
     * Parses a value from a text source.
     *
     * @param value Source value.
     * @return Parsed value.
     * @throws DatatypeConverterException If an exception occurs.
     */
    public T parse(String value) throws DatatypeConverterException;

    /**
     * Retrieves a text-based object's representation.
     *
     * @param value Object value.
     * @return Text-based object's representation.
     * @throws DatatypeConverterException If an exception occurs.
     */
    public String format(T value) throws DatatypeConverterException;
}
