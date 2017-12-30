package net.apercova.quickcli.api;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import net.apercova.quickcli.api.CommandFactory;
import net.apercova.quickcli.api.TaskCommand;

public class GetDateTest {
	String[] args;
	
	@Before
	public void init() {
		args = new String[] {};
	}
	
	@Test
	public void test() throws Exception {
		TaskCommand<Date> command = CommandFactory.createCommand(args, GetDate.class);	
		assertTrue(command != null);
		System.out.println(command.toString());
				
		
		Date result = command.execute();
		
		assertTrue("Error printing current date", result != null);
		System.out.println(result);
		
	}
}
