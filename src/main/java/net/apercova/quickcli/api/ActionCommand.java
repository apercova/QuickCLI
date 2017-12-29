package net.apercova.quickcli.api;

/**
 * Command that represents an action with no execution's return value
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface ActionCommand extends Command {

	/**
	 * Executes an action
	 * @throws Exception if an exception occurs
	 */
	public void execute() throws Exception;
}
