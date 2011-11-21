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
 * WikipediaVastaaja: tulostetaan fakta etsim�ll� wikipediasta random artikkeli
 * ja k�ytt�m�ll� artikkelin ensimm�ist� lausetta.
 * 
 * @author HV
 * 
 */
public class WikipediaVastaaja extends Vastaaja {
	/**
	 * lyhenteet on lista lyhenteist�, joiden avulla botti erottaa loppuuko
	 * lause vai onko kyse lyhenteest�
	 */
	private List<String> lyhenteet;

	/**
	 * M��ritet��n luokan reaktiokomennot konstruktorissa Haetaan my�s lyhenteet
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
	 * Etsit��n lyhenteet listaan internetist�.
	 * 
	 * @throws IOException
	 *             Mahdollinen IOException
	 */
	public void etsiLyhenteet() throws IOException {
		// Yhdistet��n lyhenneluetteloon
		ArrayList<String> tempLyhenteet = new ArrayList<String>();
		// Jsoupin avulla luetaan Document tyypin tiedosto ja ker�t��n sivun
		// data sinne
		Document sivu = Jsoup.connect("http://www.kotus.fi/index.phtml?s=2149")
				.get();

		// Kaikki strong-tageilla merkityt merkkijonot ovat mahdollisia
		// lyhenteit�
		Elements lyhenteet = sivu.getElementsByTag("strong");
		Iterator<Element> itr = lyhenteet.iterator();
		/* K�yd��n l�pi mahdolliset lyhenteet */
		while (itr.hasNext()) {

			/*
			 * Hy�dynnet�� Jsoup paketin metodeita ja parsitaan elementti niin,
			 * ett� HTML-tagit poistetaan
			 */

			String tutkittavaTag = Jsoup.parse(itr.next().toString()).text()
					.toString();

			/*
			 * Jos merkkijonossa on piste, lis�t��n se v�liaikaiseen
			 * tempLyhenteet listaan
			 */

			if (tutkittavaTag.contains(".")) {
				tempLyhenteet.add(tutkittavaTag);
			}
		}

		/*
		 * Luodaan iteraattori, jolla k�yd��n lista tempLyhenteet l�pi
		 * tutkittavaString splitataan, jotta moniosaiset lyhenteet otetaan my�s
		 * huomioon. Mik�li lyhenteen osa ei ole viel� lyhenteet listassa,
		 * lis�t��n se sinne
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
			// Jos kyseess� ei ole moniosaista lyhennett�, lis�t��n se suoraan
			else {
				this.lyhenteet.add(tutkittavaString[0]);
			}
		}
	}

	/**
	 * Metodilla m��ritell��n mik�li merkkijono on lyhenne
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
	 * Metodilla k�yd��n merkkijono l�pi ja tarkistetaan, onko siin� pelk�st��n
	 * numeroita ja viimeisen� merkkin� .
	 * 
	 * @param str
	 *            tutkittava merkkijono
	 * @return true/false riippuen onko kyseess� merkkijono
	 */
	public boolean onJarjestysnumero(String str) {

		/*
		 * K�yd��n ensin merkkijono l�pi, ettei siin� ole viimeiseen merkkiin
		 * menness� muita kuin numeroita
		 */

		for (int i = 0; i < str.length() - 1; i++) {
			if ((i < str.length() - 1) && !Character.isDigit(str.charAt(i))) {
				// jos on palautetaan false
				return false;
			}
		}
		// tarkitetaan, ett� viimeinen merkki on .
		if (str.charAt(str.length() - 1) == '.') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Metodilla hankkiudutaan ensimm�isess� lauseessa usein sijaitsevista
	 * suluista eroon. Tavoitteena on, ett� esimerksiksi seuraava lause: "Johann
	 * Sebastian Bach (21. maaliskuuta 1685 Eisenach � 28. hein�kuuta 1750
	 * Leipzig) oli saksalainen s�velt�j�, kapellimestari ja urkuri." n�ytt�isi
	 * t�lt�: "Johann Sebastian Bach oli saksalainen s�velt�j�, kapellimestari
	 * ja urkuri." T�ll� tavalla botti kuulostaa enemm�n ihmism�iselt�.
	 * 
	 * @param str
	 *            k�sitelt�v� merkkijono
	 * @return merkkijono, ilman sulkuja ja niiden sis�lt��
	 */
	public String leikkaaSulut(String str) {

		/* luodaan merkkijonosta taulukko ja alustetaan ArrayList: lauseLista */

		String[] sanat = str.split(" ");
		ArrayList<String> lauseLista = new ArrayList<String>();

		/* Lis�t��n kaikki taulukossa olevat sanat listaan */
		int x = 0;
		while (x < sanat.length) {
			lauseLista.add(sanat[x]);
			x++;
		}

		/*
		 * Luodaan StringBuilder, sek� m��ritell��n paikallismuuttuja, jota
		 * m��rittelem��n, poistetaanko sana vai ei. Luodaan my�s iteraattori
		 * listan l�pik�ymiseen.
		 */

		StringBuilder builder = new StringBuilder();
		boolean onPoistettava = false;
		Iterator<String> tutkitaanSanat = lauseLista.iterator();
		// K�yd��n sanat l�pi
		while (tutkitaanSanat.hasNext()) {
			String tutkittavaSana = tutkitaanSanat.next();
			// Jos sana alkaa ( ja p��ttyy ) sulkuun, onPoistettava = false
			if (tutkittavaSana.startsWith("(") && tutkittavaSana.endsWith(")")) {
				onPoistettava = false;
			}
			// Jos sana alkaa (-sululla, onPoistettava = true
			else if (tutkittavaSana.startsWith("(")) {
				onPoistettava = true;
			}
			/*
			 * Jos sana p��ttyy )-sulkuun, poistetaan kyseinen sana ja asetetaan
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
			// Lis�t��n builderiin joka kierros tarkasteltavana oleva sana
			builder.append(itr.next()).append(' ');
		}
		// Muutetaan stringbuilder stringiksi ja palautetaan se
		return builder.toString();
	}

	/**
	 * Wikipedian teksteiss� saattaa olla joskus viitteit�, merkittyn�
	 * hakasulkujen sis��n. Esim blaablaa[1]. T�m�n metodin avulla poistetaan
	 * nuo hakasulut. Metodilla eliminoidaan my�s vaihtoehto, ett� fakta alkaisi
	 * sanalla Koordinaatit: x E y N, sill� oikea kappale alkaa vasta niiden
	 * j�lkeen.
	 * 
	 * @param str
	 *            Merkkijono, josta viitteet tulee poistaa
	 * @return merkkijono ilman viitteit�
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
			 * Melko purkkaratkaisu, mutta t�ll� tavalla p��st��n kolmesta
			 * ensimm�isest� sanasta eroon.
			 */

			for (int i = 0; i < 3; i++) {
				sanat[i] = "";
			}
		}

		/* K�yd��n sanat taulukko l�pi */

		for (int x = 0; x < sanat.length; x++) {

			/*
			 * Jos tarkasteltavassa sanassa on [-sulku, otetaan sanasta
			 * substring joka alkaa alusta ja p��ttyy [-sulun indexiin. T�m�
			 * voidaan tehd�, koska viitteet sijaitsevat aina sanojen per�ss�.
			 */

			if (sanat[x].contains("[")) {
				sanat[x] = sanat[x].substring(0, sanat[x].indexOf("["));
				sb.append(' ').append(sanat[x]);
			}

			/*
			 * Mik�li wikipediassa on j�tettu l�hdeviittaus tekem�tt�, merkit��n
			 * sit� linkill� l�hde?. Kyseinen linkki halutaan kuitenkin poistaa
			 * merkkijonoista. T�m� tehd��n samalla periaatteella, kun
			 * []-sulkujen poisto
			 */

			else if (sanat[x].contains("l�hde?")) {
				sanat[x] = sanat[x].substring(0, sanat[x].indexOf("l�hde?"));
				sb.append(' ').append(sanat[x]);
			}
			/*
			 * Jos mit��n yll�olevista ei havaittu, voidaan sana vapaasti lis�t�
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
	 * Metodilla muunnetaan Wikipedian random-sivu tekstimuotoon. T�m�n lis�ksi
	 * poimitaan artikkelin ensimm�inen lause.
	 * 
	 * @return random artikkelin ensimm�inen lause merkkijonona
	 * @throws IOException
	 *             heitet��n mahdollinen IOException
	 */
	public String muunnaHtmlTextiksi() throws IOException {

		/*
		 * Jsoup pakettia apua k�ytt�en yhdistet��n sivulle, joka antaa
		 * Wikipediasta random artikkelin
		 */

		Document doc = Jsoup.connect(
				"http://fi.wikipedia.org/wiki/Special:Random").get();

		/*
		 * Jsoupin Element luokkaa apuna k�ytt�en, poimitaan koko sivulla
		 * ensimm�inen ilmeentynyt <p> tagi. T�m� p�tee yli 98% Wikipedian
		 * sivuista
		 */

		Element kappale = doc.getElementsByTag("p").first();

		/*
		 * Jos ensimm�inen kappale palauttaa null, haetaan uusi artikkeli ja
		 * tehd��n samat toimen piteet
		 */

		while (kappale == null) {
			doc = Jsoup.connect("http://fi.wikipedia.org/wiki/Special:Random")
					.get();
			kappale = doc.getElementsByTag("p").first();
		}

		/*
		 * Luodaan stringbuilder ja buffered reader, joilla l�hdet��n
		 * k�sittelem��n sivulta haettua tietoa
		 */

		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new StringReader(
				kappale.toString()));
		String rivi = br.readLine();
		/* K�yd��n rivi yksi kerrallaan l�pi ja lis�t��n stringbuilderiin */
		while (rivi != null) {
			sb.append(rivi);
			rivi = br.readLine();
		}
		// Luodaan faktalauseen stringbuilderi
		StringBuilder faktaLause = new StringBuilder();

		/*
		 * Luodaan sanat taulukko faktalauseesta, joka on k�ynyt l�pi kaikki
		 * mahdolliset k�sittelyt, eli: poistetaan viitteet, sulut, poistetaan
		 * html tagit, muutetaan tekstimuotoon ja merkkijonoksi, korvataan
		 * kaikki ajatusviivat tavallisella viivalla ja
		 */

		String[] sanat = this.poistaViitteet(
				this.leikkaaSulut(Jsoup.parse(sb.toString()).text())
						.replaceAll("\\p{Pd}", "-")).split(" ");

		/* K�yd��n sanat taulukko l�pi */

		for (int i = 0; i < sanat.length; i++) {

			/*
			 * Jos sana p��ttyy pisteeseen, ja sanan pituus on suurempi kuin
			 * 2(ei ole kyse nimikirjaimesta) ja sana ei ole lyhenne
			 */

			if (sanat[i].endsWith(".") && sanat[i].length() > 2
					&& !this.onLyhenne(sanat[i])
					&& !this.onJarjestysnumero(sanat[i])) {

				/*
				 * Jos listassa on viel� sana t�m�n sanan j�lkeen ja se alkaa
				 * isolla alkukirjaimella(lause p��ttyy), niin palautetaan
				 * siihen asti konstruktoitu merkkijono
				 */

				if ((i + 1) < sanat.length
						&& this.tarkistaAlkukirjain(sanat[i + 1])) {
					faktaLause.append(' ').append(sanat[i]);
					return faktaLause.toString().trim();
				}
			}
			// Muuten sana lis�t��n merkkijonoon
			faktaLause.append(' ').append(sanat[i]);
		}

		/*
		 * Jos ehtoja ei t�ytetty, palautetaan null. Generoi vastaus pit��
		 * huolen, ett� t�ll�in arvotaan uusi artikkeli
		 */

		return null;
	}

	/**
	 * Toteutetaan abstarktin luokan Vastaajan metodi generoiVastaus
	 */
	public String generoiVastaus(String viesti, String lahettaja) {
		// L�ht�kohtaisesti vastaus on null
		String vastaus = null;
		// Jatketaan kunnes vastaus on jotain muuta kuin null
		while (vastaus == null) {
			// catchataan mahdollinjen IOException
			try {
				// M��ritell��n, ett� vastaus haetaan randomilla netist�
				vastaus = this.muunnaHtmlTextiksi();

				/*
				 * Wikipediassa on mahdollista, ett� joissain tapauksissa, kun
				 * ensimm�inen lause haetaan, ei tulostetakaan faktaa
				 * pelaajasta, vaan t�m�n tilastoista. T�ll�in Wikipedian
				 * muotoilus��nt�jen mukaisesti kyseiset lauseet alkavat
				 * "Seurajoukkueuran tilastot kattavat...". T�m� lause ei
				 * kuitenkaan pid� itsess��n sis�ll��n yht��n faktaa. N�in
				 * ollen, jos fakta pit�� sis�ll��n kyseisen merkkijonon, niin
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

	public static void main(String[] args) throws IOException {
		WikipediaVastaaja w = new WikipediaVastaaja();
		System.out.println(w.generoiVastaus("asd", "lahettaja"));
		//System.out.println(w.onJarjestysnumero("3453."));
	}
}
