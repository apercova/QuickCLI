package net.apercova.quickcli.api;

/**
 * Base abstract task command
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public abstract class BaseTaskCommand<T> extends BaseCommand implements TaskCommand<T>{
	
	public T execute() throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
}
