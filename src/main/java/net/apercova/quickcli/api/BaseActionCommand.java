package net.apercova.quickcli.api;

/**
 * Base abstract action command
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public abstract class BaseActionCommand extends BaseCommand implements ActionCommand{
	
	public void execute() throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
}
