package trolotov;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Teini heittää teinimäisiä kommentteja.
 */
public class Teini extends Vastaaja {

	private static final Random rand = new Random();

	private List<String> kivat;

	private List<String> ilkeet;

	private List<String> ranteetAuki;

	/**
	 * Luonti-metodi luo 3 taulukkoa, kivatTaulukossa vastaukset hyville
	 * tyypeille, ilkeetTaulukko vastaukset ei-kivoille tyypeille,
	 * aukiTaulukkossa on taas loput angsti kommentit
	 */
	public Teini() {
		String[] kivatTaulukko = new String[] { "sä oot niin ihQ!!<3",
				"siis love u 4ever!!!!1", "<333333", "loveU 4ever",
				"sä oot mun bestis", "I<3U", "aww aww aww",
				":3 :3 :3 :3 :3 :3 :3", "W00T", "puspus 1",
				"EN KESTÄ OOT MUN JUMALA :-----------------------DDDDDDDDDDD",
				"raxutan sun hiuxii!!11 <3 otakuvaotakuvaotakuva. :< ", };
		kivat = Arrays.asList(kivatTaulukko);
		String[] ilkeetTaulukko = new String[] { "sä oot niin lame",
				"vittu mä hajoon suhun", "WTF!?! mitä sä kelaat?!", "ROF LOL",
				"sä oot niin teinixpissix!", "vitun n00b", "mutsis on..",
				":-----------D"

		};
		ilkeet = Arrays.asList(ilkeetTaulukko);
		String[] aukiTaulukko = new String[] {
				"vittu mitä paskaa?!!!1",
				"vittu mä hajoon!",
				"trololololol",
				"MIKS VITUS SE VITUN KRISTEN STEWART EI OSAA PITÄÄ SITÄ "
						+ "VITUN SUUTANSA KIINNI????? ",
				"ARGH SORI TOI YKS VIESTI KU TÄÄ SEKOO D:",
				"omfg mun Q tuli TÄNÄÄN D:::", };
		ranteetAuki = Arrays.asList(aukiTaulukko);
	}

	@Override
	public boolean viestiKiinnostaa(String viesti, String lahettaja) {
		return true;
	}

	/**
	 * generoi vastauksen naamakertoimen mukaan
	 */
	public String generoiVastaus(String viesti, String lahettaja) {
		double i = rand.nextInt();
		int naamakerroin = NaamakerroinLaskija.annaNaamakerroin(lahettaja);
		if (naamakerroin > 8 && i > 0.5) {
			return kivat.get(rand.nextInt(kivat.size() - 1));
		} else if (naamakerroin <= 2 && i > 0.4) {
			return ilkeet.get(rand.nextInt(ilkeet.size() - 1));
		} else if (naamakerroin <= 8 && naamakerroin > 5) {
			if (i > 0.9) {
				return kivat.get(rand.nextInt(kivat.size() - 1));
			}
			if (i < 0.03) {
				return ilkeet.get(rand.nextInt(ilkeet.size() - 1));
			}
		} else if (naamakerroin < 5 && naamakerroin > 2) {
			if (i > 0.9) {
				return ilkeet.get(rand.nextInt(ilkeet.size() - 1));
			}
		}
		// ranteet vedetään auki jos muut eivät onnistu
		return ranteetAuki.get(rand.nextInt(ranteetAuki.size() - 1));
	}

	/**
	 * Staattinen metodi joka kääntää kaiken teinix-kieleksi.
	 * 
	 * @param viesti
	 *            Tavallinen teksti.
	 * @return TEiNiGX-viesti hei Daa!1
	 */
	public static String muutaTeinix(String viesti) {
		if (viesti == null) {
			return null;
		}
		String vastaus = viesti;
		if (rand.nextInt(100) < 35) { // 35%
			String alku = rand.nextBoolean() && !viesti.contains("vittu") ? "no vittu " : "siis hei, ";
			vastaus = alku.concat(vastaus);
		}
		vastaus = vastaus.toLowerCase();
		vastaus = vastaus.replace("ks", "X");
		vastaus = vastaus.replace("ku", "Q");
		if (rand.nextInt(100) < 80) { // 80%
			vastaus = vastaus.replace(" on ", " on n11nQ ");
			vastaus = vastaus.replace(" oli ", " oli sillee ");
			vastaus = vastaus.replace(" joka ", " joka siis D44 ");
		}
		if (vastaus.endsWith(".")) {
			return vastaus.substring(0, vastaus.length() - 1);
		}

		return vastaus;
	}
	// Siis daa… lovex 4ever!!
	// Siis emmä mikää nörtti oo?
	// XDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
	// ^^
	// Siis ku mä olin täs (tänää/yks maanantai/eile) (stadis/skolel/dösäril) ja
	// (bostasin/hengasin/chillailin)(frendin/mutsin/faija/ yhen nevarin) kaa ni
	// siis (XD/ LOL/ LOLOLLOL/ WTF?!?!) mä (tsiigasin/ tsekkasin) yht
	// (heeboo/jäbää/tyyppii) ja siis (XD/ LOL/ LOLOLLOL/ OMG/ ihquu) se oli
	// (vittu/vitu/prkl) (HV siis Henri Virtanen/ Cheek/ Antti Tuisku)!!!!
	//
	// Sä oot niin lame, emmä sulle puhu!
	//
	// No siis vink vink, mitä sul ois illal plääneis?
	// -> Meil ois niiku käty ;DDDDDDD
	// -> voitais pitää pikku
	// (bileet/juhlat/partyt/naamiaiskemut/lanisessiot/illalliset)… Täst tulee
	// sairaan mageet!!
	// -> mitäs funtsit?
	// Se ois sit meil seiskan kieppeillä :DDDDDD
	//
	// Siiis awwwwwwwww….
}