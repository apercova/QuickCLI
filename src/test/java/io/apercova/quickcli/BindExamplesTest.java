package net.apercova.quickcli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import io.apercova.quickcli.CommandFactory;
import io.apercova.quickcli.command.BindExamples;

public class BindExamplesTest {

    String[] args;

    @Before
    public void init() {
        args = new String[]{
            "--string", "sha1",
            "--boolean",
            "--byte", String.valueOf(Byte.MAX_VALUE),
            "--short", String.valueOf(Short.MAX_VALUE),
            "--int", String.valueOf(Integer.MAX_VALUE),
            "--long", String.valueOf(Long.MAX_VALUE),
            "--float", String.valueOf(Float.MAX_VALUE),
            "--double", String.valueOf(Double.MAX_VALUE),
            "--big-integer", String.valueOf(Long.MAX_VALUE + Long.MAX_VALUE),
            "--big-decimal", String.valueOf(Long.MAX_VALUE * 1.23),
            "--cs", "iso-8859-1",
            "--help"
        };
    }

    @Test
    public void test() throws Exception {
        BindExamples command = CommandFactory.createCommand(args, BindExamples.class, new Locale("es"));
        assertTrue(command != null);

        assertEquals(Byte.MAX_VALUE, command.getBits());
        assertEquals(Short.MAX_VALUE, command.getCorto());
        assertEquals(Integer.MAX_VALUE, command.getEntero());
        assertEquals(Long.MAX_VALUE, command.getLargo());
        assertTrue(new Float(Float.MAX_VALUE).equals(command.getFlotante()));
        assertTrue(new Double(Double.MAX_VALUE).equals(command.getDoble()));
        assertEquals(new BigInteger((Long.MAX_VALUE + Long.MAX_VALUE) + ""), command.getBint());
        assertEquals(new BigDecimal((Long.MAX_VALUE * 1.23) + ""), command.getBdec());

        assertEquals(Charset.forName("iso-8859-1"), command.getCharset());

        command.execute();

    }
}
