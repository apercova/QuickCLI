package net.apercova.quickcli.api;

/**
 * Command that represents a task with execution's return value
 * @param <T> Type for execution's return value
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface TaskCommand<T> extends Command {

	/**
	 * Executes a task
	 * @return T Execution result
	 * @throws Exception if an exception occurs
	 */
	public T execute() throws Exception;
}
