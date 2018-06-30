package net.apercova.quickcli.examples.command;

import net.apercova.quickcli.ActionCommand;
import net.apercova.quickcli.CLIArgument;
import net.apercova.quickcli.CLICommand;

/**
 * Example of action command listing all JVM properties.
 *  
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 *
 */
@CLICommand(value="osversion", description="Prints OS Version")
public class JVMProps extends ActionCommand {
	
	@CLIArgument(name="--prop-name")
	private String propName;
	
	public void execute() throws Exception {
		writer.println("-----Begin JVM Properties-----");
		if(propName != null && propName.length() != 0) {
			String name = String.valueOf(propName);
			name = name.substring(0, (name.length() >= 35? 35: name.length()));
			writer.printf(locale, "%-35s|%s%n", name, System.getProperties().getProperty(name));
		}else {
			for(Object propName: System.getProperties().keySet()) {
				String name = String.valueOf(propName);
				name = name.substring(0, (name.length() >= 35? 35: name.length()));
				writer.printf(locale, "%-35s|%s%n", name, System.getProperties().getProperty(name));
			}
		}
		writer.println("-----End JVM Properties-----");
	}
}
