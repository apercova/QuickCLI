package com.github.apercova.quickcli;

import com.github.apercova.quickcli.exception.ExecutionException;

/**
 * Executable object.Represents an executable object.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @param <T> Execution result type
 * @since 1.0
 *
 */
public interface Executable<T> {

    /**
     * Run execution.
     *
     * @return T execution result.
     * @throws ExecutionException if an exception occurs.
     */
    public T execute() throws ExecutionException;

}
