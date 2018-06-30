package net.apercova.quickcli.examples.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import net.apercova.quickcli.CommandFactory;
import net.apercova.quickcli.Task;

public class GetDateTest {
	String[] args;
	
	@Before
	public void init() {
		args = new String[] {"-l","pt_PT","-f","dd/MMMM/yyy"};
	}
	
	@Test
	public void test() throws Exception {
		Task<String> command = CommandFactory.createCommand(args, GetDate.class);	
		assertTrue(command != null);				
		
		String result = command.execute();
		assertNotNull(result != null);
		
		DateFormat df = new SimpleDateFormat("dd/MMMM/yyy", command.getLocale());
		assertEquals(df.format(Calendar.getInstance().getTime()), result);
	}
}
