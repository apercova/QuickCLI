package net.apercova.quickcli;

/**
 * Represents an exception occurred when parsing arguments
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class CLIArgumentException extends Exception{

	private static final long serialVersionUID = 9180935109838301089L;

	public CLIArgumentException() {
		super();
	}

	public CLIArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public CLIArgumentException(String message) {
		super(message);
	}

	public CLIArgumentException(Throwable cause) {
		super(cause);
	}

	
}
