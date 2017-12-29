package net.apercova.quickcli.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Datatype converter for {@link Date}
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class SimpleDateConverter implements DatatypeConverter<Date> {

	public final static DateFormat ISO_ZULU_DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	public final static DateFormat ISO_COMBINED_DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public final static DateFormat ISO_DATE_DF = new SimpleDateFormat("yyyy-MM-dd");
	public final static DateFormat ISO_TIME_DF = new SimpleDateFormat("HH:mm:ss");
	
	public Date parse(String value) throws DatatypeConverterException {
		
		try {
			try {
				return ISO_ZULU_DF.parse(value);
			} catch (ParseException e) {
				try {
					return ISO_COMBINED_DF.parse(value);
				} catch (ParseException e2) {
					try {
						return ISO_TIME_DF.parse(value);
					} catch (ParseException e3) {
						try {
							return ISO_DATE_DF.parse(value);
						} catch (ParseException e4) {
							return DateFormat.getDateInstance().parse(value);
						}
					}
				}
			}
		} catch (ParseException e) {
			throw new DatatypeConverterException( String.format("Not a valid date: %s", value) , e);
		}
		
	
	}

	public String marshall(Date value) {
			return ISO_ZULU_DF.format(value);
	}
	
}
