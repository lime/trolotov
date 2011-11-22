package trolotov;

import java.util.regex.*;

/**
 * Kertoo käyttäjille niiden (tarkasti valitun) naamakertoimen.
 * 
 * @author Senja
 * 
 */
public class NaamakerroinVastaaja extends Vastaaja {
	/**
	 * Luo uuden naamakerroinvastaajan. Reagoi komentoon
	 * <code>!naamakerroin</code>.
	 */
	public NaamakerroinVastaaja() {
		this.reaktioKomennot = new String[] { "!naamakerroin" };
	}

	public String generoiVastaus(String viesti, String lahettaja) {
		String tyyppi = null;
		try {
			// Jaetaan viesti osiin välilyönnin perusteella
			String[] osat = viesti.split("\\s");
			if (osat.length > 1) {
				// tyyppi on komennon toka osa
				tyyppi = osat[1];
			}
		} catch (PatternSyntaxException pse) {
			System.err.println("Nyt tuli virhe naamakertoimen kysymisestä."
					+ " Ei ollu iha oikee komento tj!");
		}

		// Jos tyyppiä ei oltu saatu viestistä jotain, tyyppi on lähettäjä
		// joka kysyy omaa naamakerrointaan
		if (tyyppi == null) {
			tyyppi = lahettaja;
		}
		int naamakerroin = NaamakerroinLaskija.annaNaamakerroin(tyyppi);
		if (naamakerroin == -1) {
			return "ei oo vittu tollast tyyppii tääl";
		} else if (naamakerroin < 5) {
			return "toi " + tyyppi
					+ " on iha paska jätkä, sen naamakerroin " + "on "
					+ naamakerroin;
		} else if (naamakerroin < 7) {
			return "joo " + tyyppi + " on s11s tosi IhQ, sen naamis on "
					+ naamakerroin + " :)";
		} else if (naamakerroin < 11) {
			return "no siis DAA, " + tyyppi + " on maailman ihanin raXu poXu "
					+ "sen naamakerroin on superupee " + naamakerroin + "!!11";
		}
		return "en tajuu!!11";
	}
}