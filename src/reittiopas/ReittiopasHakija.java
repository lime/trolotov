package reittiopas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author eml
 * 
 */
public class ReittiopasHakija {

	private final String alkuosaURL;
	private static CharsetDecoder decoder;
	private static CharsetEncoder encoder;

	static {
		Charset charset = Charset.forName("ISO-8859-1");
		decoder = charset.newDecoder();
		encoder = charset.newEncoder();
		
		encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
	}

	public ReittiopasHakija() {
		this.alkuosaURL = "http://api.reittiopas.fi/public-ytv/fi/api/?user=olotov&pass=7r01070v";
	}

	private Reitti haeReitti(ReittiOsoite mistaOsoite, ReittiOsoite mihinOsoite)
			throws IOException {
		return this.haeReitti(mistaOsoite, mihinOsoite, null, true);
	}

	/**
	 * Palauttaa ensimmäisen reittivaihtoehdon.
	 * 
	 * @param mistaOsoite
	 * @param mihinOsoite
	 * @param aika
	 * @param onLahtoAika
	 * @return
	 * @throws IOException
	 */
	private Reitti haeReitti(ReittiOsoite mistaOsoite,
			ReittiOsoite mihinOsoite, String aika, boolean onLahtoAika)
			throws IOException {
		String url = alkuosaURL
				.concat("&a=" + mistaOsoite.annaX() + "," + mistaOsoite.annaY())
				.concat("&b=" + mihinOsoite.annaX() + "," + mihinOsoite.annaY());
		// saatetaan käyttää aikaa, ehkäehkä
		if (aika != null) {
			// tarkista muoto?
			url = url.concat("&time=" + aika).concat(
					"&timemode=" + (onLahtoAika ? "1" : "2"));
		}

		Document reitit = haeReittioppaanTiedosto(url);
		// palauttaa vaan ensimmäisen
		Reitti ekaReitti = new Reitti(reitit.select("ROUTE").get(0),
				mistaOsoite, mihinOsoite);
		return ekaReitti;
	}

	/**
	 * Palauttaa ensimmäisen osoitevaihtoehdon.
	 * 
	 * @param haku
	 *            Hakusana (esim osoite)
	 * @return Tällä hetkellä vain ensimmäisen vaihtoehdon.
	 * @throws IOException
	 */
	private ReittiOsoite haePaikka(String haku) throws IOException {
		String url = alkuosaURL.concat("&key=" + haku);
		Document vaihtoehdot = haeReittioppaanTiedosto(url);
		ReittiOsoite ekaOsoite;
		if (!vaihtoehdot.select("LOC").isEmpty()) {
			ekaOsoite = new ReittiOsoite(vaihtoehdot.select("LOC").first());
		} else {
			ekaOsoite = null;
		}

		return ekaOsoite;
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Document haeReittioppaanTiedosto(String url) throws IOException {
		System.out.println("ReittiopasHakija.haeReittioppaasta(), url="+url);
		return Jsoup.connect(url).get();
	}

	/**
	 * Etsii annetuista hakusanoista reitin reittiopaasta.
	 * 
	 * @param mista
	 *            Lähtöosoite/-pysäkki.
	 * @param mihin
	 *            Pääteosoite/-pysäkki.
	 * @param lahtoAika
	 *            Mahd. lähtöaika, <code>null</code> jos haetaan <i>nyt</i>.
	 * @return Reitti-olion jossa seuraava matka osoitteiden välillä.
	 */
	public Reitti reittiHaku(String mista, String mihin, String lahtoAika) {
		Reitti reitti = null;

		// hoidetaan encoding (ISO-8859-1 URLeihin)
		try {
			mista = URLEncoder.encode(mista, "ISO-8859-1");
			mihin = URLEncoder.encode(mihin, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("ReittiopasHakija.reittiHaku(): " + mista + " "
				+ mihin);

		try {
			ReittiOsoite mistaOsoite = this.haePaikka(mista);
			ReittiOsoite mihinOsoite = this.haePaikka(mihin);

			if(mistaOsoite != null && mihinOsoite != null){
				reitti = this.haeReitti(mistaOsoite, mihinOsoite);
			} else {
				return null;
			}
		} catch (IOException e) {
			// ei onnistunut
			e.printStackTrace();
			return null;
		}

		return reitti;
		// tarkista lahtoaika

	}

	/*protected static String UTFtoISO(String merkkijono) {
		System.out.println("ReittiopasHakija.UTFtoISO() ennen "+merkkijono);
		try {
			merkkijono = new String(merkkijono.getBytes("ISO-8859-1"));
			//merkkijono = encoder.encode(CharBuffer.wrap(merkkijono))
				//	.asCharBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ReittiopasHakija.UTFtoISO() jälkeen "+merkkijono);
		return merkkijono;
	}*/

	protected static String ISOtoUTF(String merkkijono) {
		try {
			merkkijono = decoder.decode(
					ByteBuffer.wrap(merkkijono.getBytes("ISO-8859-1"))).toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return merkkijono;
	}
}
