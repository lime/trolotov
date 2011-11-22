/**
 * 
 */
package reittiopas;

import java.util.ListIterator;

import org.jsoup.nodes.Element;

/** Reitti on kokonainen matka paikasta toiseen.
 * @author eml
 * 
 */
public class Reitti {

	private Element reittiOhje;
	private ReittiOsoite mistaOsoite, minneOsoite;

	/**Luo uuden reitti-olion.
	 * @param reittiOhje Reittioppaan XML-koodi joka kuvaa reittiä.
	 * @param mistaOsoite
	 * @param minneOsoite
	 */
	public Reitti(Element reittiOhje, ReittiOsoite mistaOsoite,
			ReittiOsoite minneOsoite) {
		this.reittiOhje = reittiOhje;
		this.mistaOsoite = mistaOsoite;
		this.minneOsoite = minneOsoite;
	}

	/**
	 * Tekee "fiksun" selityksen reitistä.
	 * 
	 * @return reittiselitys jota voi lähettää käyttäjälle
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
			String aika;

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
			
			if(!osamatkanViimeinen.select("ARRIVAL").isEmpty()){
				aika = osamatkanViimeinen.select("ARRIVAL").attr("TIME");
				//TODO jatka
			}

			// charset-muunnos
			seuraavanPaikanNimi = ReittiopasHakija
					.ISOtoUTF(seuraavanPaikanNimi);

			if (osaMatka.tagName().equalsIgnoreCase("WALK")) {
				selitys = selitys.concat("kävele paikkaan "
						+ seuraavanPaikanNimi + ", ");// TODO
			} else if (osaMatka.tagName().equalsIgnoreCase("LINE")) {
				String tyyppi;
				String numero;

				// numerot tyylikkäämmin
				if (!osaMatka.attr("CODE").isEmpty()) {
					numero = osaMatka.attr("CODE").substring(1, 5).trim();
					while (numero.startsWith("0")) {
						numero = numero.replaceFirst("0", "");
					}
				} else {
					numero = "";
				}
				

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
					case 12: //lähijuna vain kirjain
						numero = numero.substring(numero.length()-1);
					case 13:
						tyyppi = "juna";
						break;
					default:
						tyyppi = "bussi";
				}

				String kulkuvaline = tyyppi + (numero.isEmpty() ? "" : " ")
						+ numero;

				selitys = selitys.concat("ota " + kulkuvaline + " paikkaan "
						+ seuraavanPaikanNimi + " ja ");
			}

			if (seuraavanPaikanNimi.equalsIgnoreCase(this.minneOsoite
					.annaNimi())) {
				// joskus tulee tuplana koska HSL:n pojat eivät osaa tehdä
				// tällaista tarkistusta :)
				break;
			}
		}

		selitys = selitys.concat("oot perillä. Jee.");

		return selitys;
	}
}
