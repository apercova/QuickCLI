package net.apercova.quickcli.api;

import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import net.apercova.quickcli.api.ActionCommand;
import net.apercova.quickcli.api.CommandFactory;

public class JVMPropsTest {
	String[] args;
	
	@Before
	public void init() {
		args = new String[] {
				"--prop-name","java.runtime.version"
		};
	}
	
	@Test
	public void test() throws Exception {
		ActionCommand command = CommandFactory.createCommand(args, JVMProps.class);	
		assertTrue(command != null);
		System.out.println(command.toString());
		
		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out, true);
		command.setPrintWriter(writer);
				
		command.execute();
		String result = out.toString();
		assertTrue("Error printing JVM Properties", (result.startsWith("Begin JVM Properties")));
		System.out.println(result);
		
	}
}
