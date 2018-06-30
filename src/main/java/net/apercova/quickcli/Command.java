package net.apercova.quickcli;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Command object
 * Represents a command
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface Command {
	
	/**
	 * Display command's name
	 * @return Command's name
	 */
	public String getName();
	/**
	 * Retrieve underlying {@link PrintStream}
	 */
	public PrintWriter getWriter();
	/**
	 * Set provided {@link Writer} as the underlying {@link PrintWriter}
	 */
	public void setWriter(Writer out);
	/**
	 * Retrieve underlying {@link Locale}
	 */
	public Locale getLocale();
	/**
	 * Set underlying {@link Locale}
	 */
	public void setLocale(Locale out);
	/**
	 * Display usage info 
	 */
	public void printUsage();	
	
}
