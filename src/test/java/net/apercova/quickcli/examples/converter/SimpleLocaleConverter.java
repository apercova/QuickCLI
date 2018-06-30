package net.apercova.quickcli.examples.converter;

import java.util.Locale;

import net.apercova.quickcli.DatatypeConverter;
import net.apercova.quickcli.DatatypeConverterException;

public class SimpleLocaleConverter implements DatatypeConverter<Locale> {

	public Locale parse(String value) throws DatatypeConverterException {
		
		try {
			Locale res = null;
			if(String.valueOf(value).matches("[a-z]{2}\\_[A-Z]{2}")) {
				String[] lc = String.valueOf(value).split("\\_");
				res = new Locale(lc[0], lc[1]);
			}else {
				res = new Locale(value);
			}
			
			return res;
		} catch (Exception e) {
			throw new DatatypeConverterException(e);
		}
		
	}

	public String format(Locale value) throws DatatypeConverterException {
		try {
			return value.toString();
		} catch (Exception e) {
			throw new DatatypeConverterException(e);
		}
	}

}
