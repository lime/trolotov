/**
 * 
 */
package reittiopas;

import java.util.ListIterator;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * @author eml
 * 
 */
public class Reitti {

	private Element reittiOhje;
	private ReittiOsoite mistaOsoite, minneOsoite;

	public Reitti(Element reittiOhje, ReittiOsoite mistaOsoite,
			ReittiOsoite minneOsoite) {
		this.reittiOhje = reittiOhje;
		this.mistaOsoite = mistaOsoite;
		this.minneOsoite = minneOsoite;
	}

	/**
	 * Tekee "fiksun" selityksen reitistä.
	 * 
	 * @return
	 */
	public String generoiSelitys() {

		String selitys = "Matka " + this.mistaOsoite.annaNimi() + " - "
				+ this.minneOsoite.annaNimi() + ": ";

		ListIterator<Element> matkanOsat = this.reittiOhje.children()
				.listIterator();
		while (matkanOsat.hasNext()) {
			Element osaMatka = matkanOsat.next();

			// hankitaan seuraavan paikan nimi
			String seuraavanPaikanNimi;
			Element osamatkanViimeinen = osaMatka.children().last();
			if (osamatkanViimeinen == null) {
				continue;
			}
			if (!osamatkanViimeinen.select("NAME").isEmpty()) {
				seuraavanPaikanNimi = osaMatka.children().last().select("NAME")
						.first().attr("VAL");
			} else if (osamatkanViimeinen.hasAttr("UID")
					&& osamatkanViimeinen.attr("UID").equalsIgnoreCase("DEST")) {
				seuraavanPaikanNimi = this.minneOsoite.annaNimi();
			} else {
				// System.err.println("Reitti.generoiSelitys(): joku moka");
				seuraavanPaikanNimi = "X";
			}

			// TODO käsittele walk ja line
			if (osaMatka.tagName().equalsIgnoreCase("WALK")) {
				selitys = selitys.concat("kävele paikkaan "
						+ seuraavanPaikanNimi + ", ");// TODO
			} else if (osaMatka.tagName().equalsIgnoreCase("LINE")) {
				String tyyppi, numero;

				switch (Integer.parseInt(osaMatka.attr("TYPE"))) {
				// TODO null-check?
					case 2:
						tyyppi = "ratikka";
						break;
					case 6:
						tyyppi = "metro";
						break;
					case 7:
						tyyppi = "lautta";
						break;
					case 12:
					case 13:
						tyyppi = "juna";
						break;
					default:
						tyyppi = "bussi";
				}

				numero = osaMatka.attr("CODE"); // TODO paranna

				selitys = selitys.concat("ota " + tyyppi + " " + numero
						+ " paikkaan " + seuraavanPaikanNimi + " ja ");// TODO
			}

			if (seuraavanPaikanNimi.equalsIgnoreCase(this.minneOsoite
					.annaNimi())) {
				// joskus tulee tuplana koska HSL:n pojat eivät osaa tehdä
				// tällaista tarkistusta :)
				break;
			}
		}

		selitys = selitys.concat("oot perillä. Jee.");
		//charset-muunnos
		//selitys = ReittiopasHakija.ISOtoUTF(selitys);

		return selitys;
	}
}
