package trolotov;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Luokka, joka huolehtii laskarivastauksien lukemisesta tekstitiedostosta ja
 * niiden kertomisesta.
 * 
 * @author Mikko Latva-Käyrä
 */

public class LaskariVastaaja extends Vastaaja {
	private BufferedReader lukija;
	/**
	 * Lista, johon tallennetaan kaikki rivit, jotka sisältävät tuta-vastauksia
	 */
	private List<String> tutaRivit;
	/**
	 * Lista, johon tallennetaan kaikki rivit, jotka sisältävät matikan
	 * vastauksia
	 */
	private List<String> matikkaRivit;

	/**
	 * Luo uuden laaskarivastaajan, joka vastaa komentoon
	 * <code>!kerrovastaus</code>
	 */
	public LaskariVastaaja() {
		this.reaktioKomennot = new String[] { "!kerrovastaus" };
		try {
			lukija = new BufferedReader(new FileReader("vastaukset.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.tutaRivit = new ArrayList<String>();
		this.matikkaRivit = new ArrayList<String>();
	}

	/**
	 * Metodi lukee halutun vastauksen tekstitiedostosta vastaukset.txt. Aluksi
	 * lukijan ja while-loopin avulla tallennetut rivit jaoitellaan tutan ja
	 * matikan vastauksiin. Sitten komennoista riippuen palautetaan eri vastaus.
	 * 
	 * @param kiinnostavaLause
	 *            kommentti, irkistä, joka kiinnostaa LaskariVastaavaa
	 * @return palauttaa tiedostosta luetun vastauksen
	 * @throws IOException
	 *             lukija heittää IOExceptionin
	 */
	public String lueVastausTiedostosta(String kiinnostavaLause)
			throws IOException {
		String osat[] = kiinnostavaLause.split(" ");
		String rivi;

		while ((rivi = lukija.readLine()) != null) {
			if (rivi.contains("$")) {
				tutaRivit.add(rivi);
			} else if (rivi.contains("#")) {
				matikkaRivit.add(rivi);
			}
		}

		if (osat.length == 2 && osat[1].equals("tuta")) {
			if (tutaRivit.isEmpty()) {
				return "Vastauksia ei löytynyt.";
			} else {
				return this.siisti(tutaRivit.get(tutaRivit.size() - 1));
			}
		}

		else if (osat.length == 2 && osat[1].equals("matikka")) {
			if (matikkaRivit.isEmpty()) {
				return "Vastauksia ei löytynyt.";
			} else {
				return this.siisti(matikkaRivit.get(matikkaRivit.size() - 1));
			}
		}

		/**
		 * tätä käytetää esim. komennolla "!kerrovastaus tuta HV", jolloin se
		 * etsii tietokannasta uusimman HV:n lisäämän vastauksen ja kertoo sen.
		 * Tämä tehdään ns. käänteisellä for-loopilla (tapa oli oma keksimä),
		 * jolloin saadaan uusin lisäys, koska uusin lisätään viimeisenä listaan
		 * tutaRivit.
		 */
		else if (osat.length == 3 && osat[1].equals("tuta")
				&& !this.tutaRivit.isEmpty()) {
			for (int i = tutaRivit.size() - 1; i >= 0; i--) {
				if (tutaRivit.get(i).contains(osat[2])) {
					return this.siisti(tutaRivit.get(i));
				}
			}
			return null;
		}

		// sama periaate kuin yllä olevassa else if -lauseessa
		else if (osat.length == 3 && osat[1].equals("matikka")
				&& !this.matikkaRivit.isEmpty()) {
			for (int i = matikkaRivit.size() - 1; i >= 0; i--) {
				if (matikkaRivit.get(i).contains(osat[2])) {
					return this.siisti(matikkaRivit.get(i));
				}
			}
			return null;
		}

		/**
		 * Tämä if-lause palauttaa 3. sanana kommentissa olleella indeksillä
		 * olevan tutan vastaukset.
		 */
		else if (osat.length == 3 && osat[1].equals("tuta")
				&& !this.tutaRivit.isEmpty()) {
			try {
				int otettuLuku = Integer.parseInt(osat[2]);
				if (otettuLuku > 0 && otettuLuku < tutaRivit.size()) {
					return this.siisti(tutaRivit.get(otettuLuku));
				} else {
					return "Vastauksia ei löytynyt.";
				}
			} catch (NumberFormatException ex) {
				return null;
			}
		}

		// sama periaate kuin yllä
		else if (osat.length == 3 && osat[1].equals("matikka")
				&& !this.matikkaRivit.isEmpty()) {
			int otettuLuku = Integer.parseInt(osat[2]);
			if (otettuLuku > 0 && otettuLuku < matikkaRivit.size()) {
				return this.siisti(matikkaRivit.get(otettuLuku));
			} else {
				return "Vastauksia ei löytynyt.";
			}
		}

		// jos mikään näistä ei täsmää, palautetaan null.
		else {
			return null;
		}
	}

	/**
	 * metodi, joka "siistii" Stringin, eli poistaa siitä alusta turhat merkit.
	 * 
	 * @param siistittava
	 *            Siistittävä String
	 * @return siistitty String
	 */
	public String siisti(String siistittava) {
		String[] splitattu = siistittava.split(" ");
		return siistittava.substring(splitattu[0].length() + 1).trim();
	}

	@Override
	/**
	 * generoi vastauksen, käyttämällä metodia lueVastausTiedostosta. Catchaa
	 * IOExceptionin.
	 */
	public String generoiVastaus(String viesti, String lahettaja) {
		try {
			return this.lueVastausTiedostosta(viesti);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * public static void main(String[] args) { LaskariVastaaja l = new
	 * LaskariVastaaja();
	 * System.out.println(l.generoiVastaus("!kerrovastaus tuta HV", "asdasd"));
	 * }
	 */
}
