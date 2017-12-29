package net.percova.console.cli.api;

import net.apercova.quickcli.api.BaseActionCommand;
import net.apercova.quickcli.api.CLIArgument;
import net.apercova.quickcli.api.CLICommand;

@CLICommand(value="osversion", description="Prints OS Version")
public class JVMProps extends BaseActionCommand {
	
	@CLIArgument(name="--prop-name")
	private String propName;
	
	@Override
	public void execute() throws Exception {
		out.println("Begin JVM Properties");
		if(propName != null && !propName.isEmpty()) {
			out.println(System.getProperties().getProperty(propName));
		}else {
			System.getProperties().list(out);
		}
		out.println("End JVM Properties");
		
		
	}
}
