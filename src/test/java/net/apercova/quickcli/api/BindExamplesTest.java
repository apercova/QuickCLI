package net.percova.console.cli.api;

import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import net.apercova.quickcli.api.Command;
import net.apercova.quickcli.api.CommandFactory;

public class BindExamplesTest {
	String[] args;
	
	@Before
	public void init() {
		args = new String[] {
				"--string","sha1",
				"--boolean",
				"--byte",String.valueOf(Byte.MAX_VALUE),
				"--short",String.valueOf(Short.MAX_VALUE),
				"--int",String.valueOf(Integer.MAX_VALUE),
				"--long",String.valueOf(Long.MAX_VALUE),
				"--float",String.valueOf(Float.MAX_VALUE),
				"--double",String.valueOf(Double.MAX_VALUE),
				"--big-integer",String.valueOf(Long.MAX_VALUE+ Long.MAX_VALUE),
				"--big-decimal",String.valueOf(Long.MAX_VALUE*1.23),
				"--date","2017-12-24T23:59:59",
				"--cs","iso-8859-1",
				"--help"//shows help
				};
	}
	
	@Test
	public void test() throws Exception {
		Command command = CommandFactory.createCommand(args, BindExamples.class);	
		assertTrue(command != null);
		
		System.out.println(command.toString());
		
		Date d = ((BindExamples)command).getDate();
		System.out.println(d);
		assertTrue("Null date value", d != null);
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2017);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 24);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		
		assertTrue("Error al parsear fecha", c.getTime().getTime() == d.getTime());
		
		
		Charset cs = ((BindExamples)command).getCharset();
		System.out.println(cs.canEncode());
		assertTrue("Null Charset", cs != null);
		assertTrue("Null Charset", "iso-8859-1".equalsIgnoreCase(cs.name()));
		
	}
}
