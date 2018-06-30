package net.apercova.quickcli.examples.command;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import net.apercova.quickcli.Action;
import net.apercova.quickcli.CommandFactory;

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
		
		Action command = CommandFactory.createCommand(args, JVMProps.class);	
		assertTrue(command != null);
		
		StringWriter sw = new StringWriter();
		command.setWriter(sw);
		
		command.execute();
		
		assertTrue((sw.toString().startsWith("-----Begin JVM Properties-----")));
		assertTrue((sw.toString().endsWith("-----End JVM Properties-----"+System.getProperty("line.separator"))));
		
	}
}
