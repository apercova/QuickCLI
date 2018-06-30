package net.apercova.quickcli.examples.converter;

import java.util.Locale;

import net.apercova.quickcli.DatatypeConverter;
import net.apercova.quickcli.DatatypeConverterException;

public class SimpleLocaleConverter implements DatatypeConverter<Locale> {

	public Locale parse(String value) throws DatatypeConverterException {
		try {
			return new Locale(value);
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
