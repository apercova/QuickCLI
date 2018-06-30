package net.apercova.quickcli;

/**
 * Represents a task with return value
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface Task<T> extends Command {

	/**
	 * Executes a task with a return value
	 * @return T Execution result
	 * @throws Exception if an exception occurs
	 */
	public T execute() throws Exception;
}
