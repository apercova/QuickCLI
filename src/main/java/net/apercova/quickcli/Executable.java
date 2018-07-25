package net.apercova.quickcli;

/**
 * Executable object.
 * Represents an executable object.
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface Executable<T> {
	
	/**
	 * Run execution.
	 * @return T execution result.
	 * @throws ExecutionException if an exception occurs.
	 */
	public T execute() throws ExecutionException;
	
}
