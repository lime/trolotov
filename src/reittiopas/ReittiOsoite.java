package reittiopas;

import org.jsoup.nodes.Element;

/**
 * 
 */

/**
 * @author eml
 * 
 */
public class ReittiOsoite {

	private Element paikka;

	public ReittiOsoite(Element paikka) {
		if (paikka != null) {
			this.paikka = paikka;
		}
	}

	public String annaX() {
		return this.paikka.attr("x");
	}

	public String annaY() {
		return this.paikka.attr("y");
	}

	public String annaNimi() {
		String nimi = this.paikka.attr("name1");
		if (!this.paikka.attr("number").isEmpty()) {
			nimi = nimi.concat(" " + this.paikka.attr("number"));
		}
		return nimi;
	}

	public String annaKaupunki() {
		return this.paikka.attr("city");
	}

}
