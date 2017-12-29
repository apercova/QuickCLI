package net.apercova.quickcli.api;

import java.nio.charset.Charset;

/**
 * Datatype converter for {@link Charset}
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class SimpleCharsetConverter implements DatatypeConverter<Charset> {

	public Charset parse(String value) throws DatatypeConverterException { 
		
		try {
			return Charset.forName(value);
		} catch (Exception e) {
			throw new DatatypeConverterException( String.format("For charset: %s", value), e);
		}

	}

	public String marshall(Charset value)  {
		return value != null? value.name():"";
	}
}
