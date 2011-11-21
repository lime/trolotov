package reittiopas;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author eml
 * 
 */
public class ReittiopasHakija {

	private final String alkuosaURL;

	public ReittiopasHakija() {
		alkuosaURL = "http://api.reittiopas.fi/public-ytv/fi/api/?user=olotov&pass=7r01070v";
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
		try {
			ReittiOsoite mistaOsoite = this.haePaikka(mista);
			ReittiOsoite mihinOsoite = this.haePaikka(mihin);

			reitti = this.haeReitti(mistaOsoite, mihinOsoite);
		} catch (IOException e) {
			// ei onnistunut
			return null;
		}

		return reitti;
		// tarkista lahtoaika

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

		Document reitit = Jsoup.connect(url).get();
		// palauttaa vaan ensimmäisen
		Reitti ekaReitti = new Reitti(reitit.select("ROUTE").get(0), mistaOsoite, mihinOsoite);
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
		Document vaihtoehdot = Jsoup.connect(url).get();
		ReittiOsoite ekaOsoite = new ReittiOsoite(vaihtoehdot.select("LOC")
				.get(0));

		return ekaOsoite;
	}
}
