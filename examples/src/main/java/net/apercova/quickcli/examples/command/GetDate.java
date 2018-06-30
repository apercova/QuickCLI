package net.apercova.quickcli.examples.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import net.apercova.quickcli.CLIArgument;
import net.apercova.quickcli.CLICommand;
import net.apercova.quickcli.CLIDatatypeConverter;
import net.apercova.quickcli.TaskCommand;
import net.apercova.quickcli.examples.converter.SimpleLocaleConverter;

/**
 * Example of task command that retrieves a formated Date.
 * Format and Locale are parameterized as args.
 * Local getters are important to retrieve instance values, not parent ones.
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 */
@CLICommand(value="currdate", description="Retrieves current date.")
public class GetDate extends TaskCommand<String>{

	@CLIArgument(name="-l")
	@CLIDatatypeConverter(SimpleLocaleConverter.class)
	private Locale locale;
	
	@CLIArgument(name="-f", value="yyyy-MM-dd'T'hh:mm:ss.SSS zZ")
	private String format;
	
	public GetDate() {
	}
	
	@Override
	public Locale getLocale() {
		return locale;
	}
	
	public String getFormat() {
		return format;
	}
	
	public String execute() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);		
		return sdf.format(Calendar.getInstance().getTime());
	}
}
