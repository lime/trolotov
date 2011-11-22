package trolotov;

import java.io.UnsupportedEncodingException;

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
	}

}
