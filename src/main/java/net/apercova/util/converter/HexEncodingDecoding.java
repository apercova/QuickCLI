package net.apercova.util.converter;

/**
 * Byte to hex String encoder/decoder
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 * 
 */
public final class HexEncodingDecoding {
	private static final String hexChars = "0123456789abcdef";
	
	private HexEncodingDecoding() {}
	
	/**
	 * Custom for JDK1.0+ java versions
	 * @param bytes source bytes
	 * @return Hex String
	 */
	public static String encode(byte[] bytes) {
		if(bytes == null) {
			throw new NullPointerException("bytes");
		}
		StringBuffer sb = new StringBuffer("");
		for(byte b: bytes) {
			sb.append(hexChars.charAt((b >> 4) & 0xF));
			sb.append(hexChars.charAt((b & 0xF)));		
		}
		return sb.toString();
	}
	
	/**
	 * Custom for JDK1.0+ java versions
	 * @param hexString Hex String
	 * @return Decoded bytes
	 */
	public static byte[] decode(String hexString){
		if( hexString == null) {
			throw new NullPointerException("hexBinary");
		}
		hexString = hexString.toLowerCase();
		final int len = hexString.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + hexString);
        }
		byte[] out = new byte[len / 2];
		
		for(int i = 0; i < len; i += 2) {			
			int h = hexChars.indexOf(hexString.charAt(i));
			int l = hexChars.indexOf(hexString.charAt(i+1));
			if (h == -1 || l == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexString);
            }
			out[i/2] = (byte) (h * 16 + l);
		}
		
		return out;		
	}
	
}
