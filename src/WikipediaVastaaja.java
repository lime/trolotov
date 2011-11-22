import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * WikipediaVastaaja: tulostetaan fakta etsimällä wikipediasta random artikkeli
 * ja käyttämällä artikkelin ensimmäistä lausetta.
 * 
 * @author HV
 * 
 */
public class WikipediaVastaaja extends Vastaaja {
	/**
	 * lyhenteet on lista lyhenteistä, joiden avulla botti erottaa loppuuko
	 * lause vai onko kyse lyhenteestä
	 */
	private List<String> lyhenteet;

	/**
	 * Määritetään luokan reaktiokomennot konstruktorissa Haetaan myös lyhenteet
	 * listaan etsiLyhenteet-metodin avulla. Metodi hakee lyhenteet sivulta
	 * http://www.kotus.fi/index.phtml?s=2149
	 */
	public WikipediaVastaaja() {
		this.reaktioKomennot = new String[] { "!fakta" };
		this.lyhenteet = new ArrayList<String>();
		try {
			this.etsiLyhenteet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tarkistetaan, alkaako merkkijono isolla alkukirjaimella
	 * 
	 * @param str
	 *            Tarkastelava merkkijono
	 * @return true: Merkkijono alkaa isolla kirjaimella false: Merkkijono ei
	 *         ala isolla kirjaimella
	 */
	public boolean tarkistaAlkukirjain(String str) {
		if (Character.isLetter(str.charAt(0))
				&& !Character.isLowerCase(str.charAt(0))) {
			return true;
		}
		return false;
	}

	/**
	 * Etsitään lyhenteet listaan internetistä.
	 * 
	 * @throws IOException
	 *             Mahdollinen IOException
	 */
	public void etsiLyhenteet() throws IOException {
		// Yhdistetään lyhenneluetteloon
		ArrayList<String> tempLyhenteet = new ArrayList<String>();
		// Jsoupin avulla luetaan Document tyypin tiedosto ja kerätään sivun
		// data sinne
		Document sivu = Jsoup.connect("http://www.kotus.fi/index.phtml?s=2149")
				.get();

		// Kaikki strong-tageilla merkityt merkkijonot ovat mahdollisia
		// lyhenteitä
		Elements lyhenteet = sivu.getElementsByTag("strong");
		Iterator<Element> itr = lyhenteet.iterator();
		/* Käydään läpi mahdolliset lyhenteet */
		while (itr.hasNext()) {

			/*
			 * Hyödynnetää Jsoup paketin metodeita ja parsitaan elementti niin,
			 * että HTML-tagit poistetaan
			 */

			String tutkittavaTag = Jsoup.parse(itr.next().toString()).text()
					.toString();

			/*
			 * Jos merkkijonossa on piste, lisätään se väliaikaiseen
			 * tempLyhenteet listaan
			 */

			if (tutkittavaTag.contains(".")) {
				tempLyhenteet.add(tutkittavaTag);
			}
		}

		/*
		 * Luodaan iteraattori, jolla käydään lista tempLyhenteet läpi
		 * tutkittavaString splitataan, jotta moniosaiset lyhenteet otetaan myös
		 * huomioon. Mikäli lyhenteen osa ei ole vielä lyhenteet listassa,
		 * lisätään se sinne
		 */

		Iterator<String> lyhenneitr = tempLyhenteet.iterator();
		while (lyhenneitr.hasNext()) {
			String[] tutkittavaString = lyhenneitr.next().split(" ");
			if (tutkittavaString.length > 1) {
				for (int i = 0; i < tutkittavaString.length; i++) {
					if (!this.lyhenteet.contains(tutkittavaString[i])) {
						this.lyhenteet.add(tutkittavaString[i]);
					}
				}
			}
			// Jos kyseessä ei ole moniosaista lyhennettä, lisätään se suoraan
			else {
				this.lyhenteet.add(tutkittavaString[0]);
			}
		}
	}

	/**
	 * Metodilla määritellään mikäli merkkijono on lyhenne
	 * 
	 * @param str
	 *            Tarkasteltava merkkijono
	 * @return true/false, riippuen onko lyhenne
	 */
	public boolean onLyhenne(String str) {

		if (this.lyhenteet.contains(str)) {
			return true;
		}
		return false;
	}

	/**
	 * Metodilla käydään merkkijono läpi ja tarkistetaan, onko siinä pelkästään
	 * numeroita ja viimeisenä merkkinä .
	 * 
	 * @param str
	 *            tutkittava merkkijono
	 * @return true/false riippuen onko kyseessä merkkijono
	 */
	public boolean onJarjestysnumero(String str) {

		/*
		 * Käydään ensin merkkijono läpi, ettei siinä ole viimeiseen merkkiin
		 * mennessä muita kuin numeroita
		 */

		for (int i = 0; i < str.length() - 1; i++) {
			if ((i < str.length() - 1) && !Character.isDigit(str.charAt(i))) {
				// jos on palautetaan false
				return false;
			}
		}
		
		
		/* Jos luvussa on neljä numeroa ja perässä piste, päättyy lause 
		 * todennäköisimmin vuosilukuun*/
		
		if (str.length() == 5) {
			return false;
		}
		
		// tarkitetaan, että viimeinen merkki on .
		if (str.charAt(str.length() - 1) == '.') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodilla hankkiudutaan ensimmäisessä lauseessa usein sijaitsevista
	 * suluista eroon. Tavoitteena on, että esimerksiksi seuraava lause: "Johann
	 * Sebastian Bach (21. maaliskuuta 1685 Eisenach  28. heinäkuuta 1750
	 * Leipzig) oli saksalainen säveltäjä, kapellimestari ja urkuri." näyttäisi
	 * tältä: "Johann Sebastian Bach oli saksalainen säveltäjä, kapellimestari
	 * ja urkuri." Tällä tavalla botti kuulostaa enemmän ihmismäiseltä.
	 * 
	 * @param str
	 *            käsiteltävä merkkijono
	 * @return merkkijono, ilman sulkuja ja niiden sisältöä
	 */
	public String leikkaaSulut(String str) {

		/* luodaan merkkijonosta taulukko ja alustetaan ArrayList: lauseLista */

		String[] sanat = str.split(" ");
		ArrayList<String> lauseLista = new ArrayList<String>();

		/* Lisätään kaikki taulukossa olevat sanat listaan */
		int x = 0;
		while (x < sanat.length) {
			lauseLista.add(sanat[x]);
			x++;
		}

		/*
		 * Luodaan StringBuilder, sekä määritellään paikallismuuttuja, jota
		 * määrittelemään, poistetaanko sana vai ei. Luodaan myös iteraattori
		 * listan läpikäymiseen.
		 */

		StringBuilder builder = new StringBuilder();
		boolean onPoistettava = false;
		Iterator<String> tutkitaanSanat = lauseLista.iterator();
		// Käydään sanat läpi
		while (tutkitaanSanat.hasNext()) {
			String tutkittavaSana = tutkitaanSanat.next();
			// Jos sana alkaa ( ja päättyy ) sulkuun, onPoistettava = false
			if (tutkittavaSana.startsWith("(") && tutkittavaSana.endsWith(")")) {
				onPoistettava = false;
			}
			// Jos sana alkaa (-sululla, onPoistettava = true
			else if (tutkittavaSana.startsWith("(")) {
				onPoistettava = true;
			}
			/*
			 * Jos sana päättyy )-sulkuun, poistetaan kyseinen sana ja asetetaan
			 * onPoistettava = false
			 */
			else if (tutkittavaSana.endsWith(")")) {
				tutkitaanSanat.remove();
				onPoistettava = false;
			}
			// Jos onPoistettava = true, poistetaan tutkittava sana listasta
			if (onPoistettava) {
				tutkitaanSanat.remove();
			}
		}

		/*
		 * Lopuksi, muodostetaan StringBuilderilla uusi String, jossa ei ole
		 * sulkuja
		 */

		Iterator<String> itr = lauseLista.iterator();
		while (itr.hasNext()) {
			// Lisätään builderiin joka kierros tarkasteltavana oleva sana
			builder.append(itr.next()).append(' ');
		}
		// Muutetaan stringbuilder stringiksi ja palautetaan se
		return builder.toString();
	}

	/**
	 * Wikipedian teksteissä saattaa olla joskus viitteitä, merkittynä
	 * hakasulkujen sisään. Esim blaablaa[1]. Tämän metodin avulla poistetaan
	 * nuo hakasulut. Metodilla eliminoidaan myös vaihtoehto, että fakta alkaisi
	 * sanalla Koordinaatit: x E y N, sillä oikea kappale alkaa vasta niiden
	 * jälkeen.
	 * 
	 * @param str
	 *            Merkkijono, josta viitteet tulee poistaa
	 * @return merkkijono ilman viitteitä
	 */
	public String poistaViitteet(String str) {

		// Splitatataan parametri str ja luodaan StringBuilder
		String[] sanat = str.split(" ");
		StringBuilder sb = new StringBuilder();
		/*
		 * Poistetaan aluksi mahdollinen virhe sivuilta, joilla julkaistaan
		 * koordinaatit
		 */

		if (sanat[0].equals("Koordinaatit:")) {

			/*
			 * Melko purkkaratkaisu, mutta tällä tavalla päästään kolmesta
			 * ensimmäisestä sanasta eroon.
			 */

			for (int i = 0; i < 3; i++) {
				sanat[i] = "";
			}
		}

		/* Käydään sanat taulukko läpi */

		for (int x = 0; x < sanat.length; x++) {

			/*
			 * Jos tarkasteltavassa sanassa on [-sulku, otetaan sanasta
			 * substring joka alkaa alusta ja päättyy [-sulun indexiin. Tämä
			 * voidaan tehdä, koska viitteet sijaitsevat aina sanojen perässä.
			 */

			if (sanat[x].contains("[")) {
				sanat[x] = sanat[x].substring(0, sanat[x].indexOf("["));
				sb.append(' ').append(sanat[x]);
			}

			/*
			 * Mikäli wikipediassa on jätettu lähdeviittaus tekemättä, merkitään
			 * sitä linkillä lähde?. Kyseinen linkki halutaan kuitenkin poistaa
			 * merkkijonoista. Tämä tehdään samalla periaatteella, kun
			 * []-sulkujen poisto
			 */

			else if (sanat[x].contains("lähde?")) {
				sanat[x] = sanat[x].substring(0, sanat[x].indexOf("lähde?"));
				sb.append(' ').append(sanat[x]);
			}
			/*
			 * Jos mitään ylläolevista ei havaittu, voidaan sana vapaasti lisätä
			 * strinbuilderiin
			 */
			else {
				sb.append(' ').append(sanat[x]);
			}
		}
		// Palautetaan strinbuilder merkkijonona
		return sb.toString().trim();
	}

	/**
	 * Metodilla muunnetaan Wikipedian random-sivu tekstimuotoon. Tämän lisäksi
	 * poimitaan artikkelin ensimmäinen lause.
	 * 
	 * @return random artikkelin ensimmäinen lause merkkijonona
	 * @throws IOException
	 *             heitetään mahdollinen IOException
	 */
	public String muunnaHtmlTextiksi() throws IOException {

		/*
		 * Jsoup pakettia apua käyttäen yhdistetään sivulle, joka antaa
		 * Wikipediasta random artikkelin
		 */

		Document doc = Jsoup.connect(
				"http://fi.wikipedia.org/wiki/Special:Random").get();

		/*
		 * Jsoupin Element luokkaa apuna käyttäen, poimitaan koko sivulla
		 * ensimmäinen ilmeentynyt <p> tagi. Tämä pätee yli 98% Wikipedian
		 * sivuista
		 */

		Element kappale = doc.getElementsByTag("p").first();

		/*
		 * Jos ensimmäinen kappale palauttaa null, haetaan uusi artikkeli ja
		 * tehdään samat toimen piteet
		 */

		while (kappale == null) {
			doc = Jsoup.connect("http://fi.wikipedia.org/wiki/Special:Random")
					.get();
			kappale = doc.getElementsByTag("p").first();
		}

		/*
		 * Luodaan stringbuilder ja buffered reader, joilla lähdetään
		 * käsittelemään sivulta haettua tietoa
		 */

		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new StringReader(
				kappale.toString()));
		String rivi = br.readLine();
		/* Käydään rivi yksi kerrallaan läpi ja lisätään stringbuilderiin */
		while (rivi != null) {
			sb.append(rivi);
			rivi = br.readLine();
		}
		// Luodaan faktalauseen stringbuilderi
		StringBuilder faktaLause = new StringBuilder();

		/*
		 * Luodaan sanat taulukko faktalauseesta, joka on käynyt läpi kaikki
		 * mahdolliset käsittelyt, eli: poistetaan viitteet, sulut, poistetaan
		 * html tagit, muutetaan tekstimuotoon ja merkkijonoksi, korvataan
		 * kaikki ajatusviivat tavallisella viivalla ja
		 */

		String[] sanat = this.poistaViitteet(
				this.leikkaaSulut(Jsoup.parse(sb.toString()).text())
						.replaceAll("\\p{Pd}", "-")).split(" ");

		/* Käydään sanat taulukko läpi */

		for (int i = 0; i < sanat.length; i++) {

			/*
			 * Jos sana päättyy pisteeseen, ja sanan pituus on suurempi kuin
			 * 2(ei ole kyse nimikirjaimesta) ja sana ei ole lyhenne
			 */

			if (sanat[i].endsWith(".") && sanat[i].length() > 2
					&& !this.onLyhenne(sanat[i])
					&& !this.onJarjestysnumero(sanat[i])) {

				/*
				 * Jos listassa on vielä sana tämän sanan jälkeen ja se alkaa
				 * isolla alkukirjaimella(lause päättyy), niin palautetaan
				 * siihen asti konstruktoitu merkkijono
				 */

				if ((i + 1) < sanat.length
						&& this.tarkistaAlkukirjain(sanat[i + 1])) {
					faktaLause.append(' ').append(sanat[i]);
					return faktaLause.toString().trim();
				}
			}
			// Muuten sana lisätään merkkijonoon
			faktaLause.append(' ').append(sanat[i]);
		}

		/*
		 * Jos ehtoja ei täytetty, palautetaan null. Generoi vastaus pitää
		 * huolen, että tällöin arvotaan uusi artikkeli
		 */

		return null;
	}

	/**
	 * Toteutetaan abstarktin luokan Vastaajan metodi generoiVastaus
	 */
	public String generoiVastaus(String viesti, String lahettaja) {
		// Lähtökohtaisesti vastaus on null
		String vastaus = null;
		// Jatketaan kunnes vastaus on jotain muuta kuin null
		while (vastaus == null) {
			// catchataan mahdollinjen IOException
			try {
				// Määritellään, että vastaus haetaan randomilla netistä
				vastaus = this.muunnaHtmlTextiksi();

				/*
				 * Wikipediassa on mahdollista, että joissain tapauksissa, kun
				 * ensimmäinen lause haetaan, ei tulostetakaan faktaa
				 * pelaajasta, vaan tämän tilastoista. Tällöin Wikipedian
				 * muotoilusääntöjen mukaisesti kyseiset lauseet alkavat
				 * "Seurajoukkueuran tilastot kattavat...". Tämä lause ei
				 * kuitenkaan pidä itsessään sisällään yhtään faktaa. Näin
				 * ollen, jos fakta pitää sisällään kyseisen merkkijonon, niin
				 * asetetaan vastaukseksi null ja arvotaan uusi fakta
				 * while-silmukan mukaisesti
				 */

				if (vastaus != null
						&& vastaus
								.contains("Seurajoukkueuran tilastot kattavat")) {
					vastaus = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return vastaus;
			}
		}
		// Lopuksi palautetaan vastaus
		return vastaus;
	}
}
