package trolotov;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 
 */

/**
 * @author eml
 *
 */
public class EncodingKaantaja {

	public static String ISOtoUTF(String viesti) {		
		try {
			if(viesti.getBytes("UTF-8").equals(viesti.getBytes())) {
				System.out.println("EncodingKaantaja.ISOtoUTF()");
				return viesti; //on jo UTF-8?
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			byte[] utf8 = new String(viesti.getBytes("ISO-8859-1"), "ISO-8859-1").getBytes("UTF-8");
			return new String(utf8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return viesti;
		}
	}

}
