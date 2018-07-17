package net.apercova.quickcli;

/**
 * Represents an action with no return value
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface Action extends Command{

	/**
	 * Executes an action
	 * @throws Exception if an exception occurs
	 */
	public void execute() throws Exception;
}
