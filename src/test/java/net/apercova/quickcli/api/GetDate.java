package net.apercova.quickcli.api;

import java.util.Calendar;
import java.util.Date;

import net.apercova.quickcli.api.BaseTaskCommand;
import net.apercova.quickcli.api.CLICommand;

@CLICommand(value="currdate", description="Retrieves current date.")
public class GetDate extends BaseTaskCommand<Date>{

	public GetDate() {
	}
	
	@Override
	public Date execute() throws Exception {
		return Calendar.getInstance().getTime();
	}
}
