package net.apercova.quickcli.api;

import java.io.PrintWriter;

/**
 * Command object
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public interface Command {

	/**
	 * Define a printwriter to output result
	 * @param out out writer
	 */
	public void setPrintWriter(PrintWriter out);
	
	/**
	 * Display usage options
	 */
	public void printUsage();	
}
