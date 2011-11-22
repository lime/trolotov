package reittiopas;

import org.jsoup.nodes.Element;

/**
 * Pitää sisällään Reittiopas-paikan (osoite, pysäkki) rakenteen ja tiedot.
 * 
 * @author eml
 * 
 */
public class ReittiOsoite {

	private Element paikka;

	/**
	 * Luo uuden reittiosoitteen.
	 * 
	 * @param paikka
	 *            XML-muotoinen paikkatieto APIsta
	 */
	public ReittiOsoite(Element paikka) {
		if (paikka != null) {
			this.paikka = paikka;
		}
	}

	/**
	 * @return paikan x-koordinaatti
	 */
	public String annaX() {
		return this.paikka.attr("x");
	}

	/**
	 * @return paikan y-koordinaatti
	 */
	public String annaY() {
		return this.paikka.attr("y");
	}

	/**
	 * @return paikan nimi muodossa katu [numero]
	 */
	public String annaNimi() {
		String nimi = this.paikka.attr("name1");
		if (!this.paikka.attr("number").isEmpty()) {
			nimi = nimi.concat(" " + this.paikka.attr("number"));
		}
		return nimi;
	}

	/**
	 * @return kaupunki jossa paikka sijaitsee
	 */
	public String annaKaupunki() {
		return this.paikka.attr("city");
	}

}
