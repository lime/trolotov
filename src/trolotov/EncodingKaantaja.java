package trolotov;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Tarkoitus että voisi kääntää viestejä charsettien välillä.
 * 
 * @author eml
 * 
 */
public class EncodingKaantaja {

	/**
	 * Yrittää (ei oikein onnistu) kääntää ISO-8859-1 charsetistä UTF-8:aan.
	 * 
	 * @param viesti
	 *            Viestiteksti
	 * @return Viestiteksti uudella charsetillä
	 */
	public static String ISOtoUTF(String viesti) {
		Charset utf8Charset = Charset.forName("UTF-8");
		Charset iso88591Charset = Charset.forName("ISO-8859-1");
		
		ByteBuffer viestiBuffer = ByteBuffer.wrap(viesti.getBytes(iso88591Charset));
		
		CharBuffer dataIso = iso88591Charset.decode(viestiBuffer);
		
		ByteBuffer kaannettyBuffer = utf8Charset.encode(dataIso);
		return new String(kaannettyBuffer.array(), utf8Charset);
	}
	/*public static String ISOtoUTF(String viesti) {
		try {
			if (viesti.getBytes("UTF-8").equals(viesti.getBytes())) {
				System.out.println("EncodingKaantaja.ISOtoUTF()");
				return viesti; // on jo UTF-8?
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			byte[] utf8 = new String(viesti.getBytes("ISO-8859-1"),
					"ISO-8859-1").getBytes("UTF-8");
			return new String(utf8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return viesti;
		}
	}*/

}
