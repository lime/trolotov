import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * KaantajaVastaaja kääntää suomenkielisiäsanoja englanniksi ja englannin
 * kielisiä sanoja suomeksi. Komennolla !fin sana_englanniksi: sana suomennetaan
 * !eng sana_suomeksi: sana käännetään englanniksi
 * 
 * @author HV
 * 
 */
public class KaantajaVastaaja extends Vastaaja {
	/**
	 * Määritetään luokan konstruktori
	 */
	public KaantajaVastaaja() {
		this.reaktioKomennot = new String[] { "!fin", "!eng" };
	}

	/**
	 * Metodi hakee käännöksen parametrin komento mukaan joko en.wiktionaryn tai
	 * fi.wiktionaryn API:sta.
	 * 
	 * @param komento
	 *            annettu komento
	 * @param sana
	 *            haettava sana
	 * @return käännös merkkijonossa
	 * @throws IOException
	 *             heittää ihan normisti IOExceptionia...
	 */
	private String haeKaannokset(String komento, String sana) throws IOException {
		String url;

		/* Tarkistetaan kumpi komento on: !fin vai !eng */

		if (komento.equals("!eng")) {
			/* Jos eng, määritetään en.wiktionary URL:ksi sanan mukaan */
			url = "http://en.wiktionary.org/w/api.php?action=query&prop=revisions|lllang=fi&titles="
					+ sana + "&rvprop=content";

			// Palautetaan käännös
			return this.kaannoksetEnglanniksi(url);

		} else if (komento.equals("!fin")) {
			// Jos fin, määritetään fi.wiktionaryn mukaan
			url = "http://fi.wiktionary.org/w/api.php?action=query&prop=revisions|lllang=fi&titles="
					+ sana + "&rvprop=content";

			// Palautetaan käännös
			return this.kaannoksetSuomeksi(url);
		}
		// Jos ei käynyt flaksi, palautetaan null
		return null;
	}

	/**
	 * Palautetaan käännökset en.wiktionarysta
	 * 
	 * @param url
	 *            Sivu, jolta haetaan käännökset
	 * @return merkkijonona käännökset
	 * @throws IOException
	 *             heittää taas IOExceptionia
	 */
	private String kaannoksetEnglanniksi(String url) throws IOException {

		/* alustetaan tarvittavat paikallismuuttujat */

		ArrayList<String> kaannoksetLista = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		// Yhdistetään Jsoup:in avulla URL:iin
		Document doc = Jsoup.connect(url).get();
		boolean voiTallentaa = false;

		/* Alustetaan bufferedreader jolla voidaan lukea ladattu sivu
		 * tekstimuodossa*/
		BufferedReader br = new BufferedReader(new StringReader(doc.toString()));
		String luettavaRivi = br.readLine();
		//Käydään kaikki rivit läpi yksi kerrallaan, kunnes tiedosto on luettu
		while (luettavaRivi != null) {
			
			/* Parsetaan rivit yksi kerrallaan niin, että poistetaan
			 * html tagit ja palautetaan teksteinä */
			
			String currentText = Jsoup.parse(luettavaRivi).text().toString();
			
			/* Jos tarkasteltavassa tekstisäs on ==Finnish==, niin mennään
			 * voiTallentaa = true. Tämä on wiktionary:n API:n tapa merkitä,
			 * minkä kielen käännökset ovat kyseessä */
			
			if (currentText.matches("(?i).*==Finnish==.*")) {
				voiTallentaa = true;
			}
			
			/* wiktionary:n API:n avulla haettuja sivuja tarkkailemalla 
			 * havaitaan, että kun ensimmäin kerran ilmestyy ==== merkkijono,
			 * ei siitä eteenpäin sisälly enää kiinnostavaa tekstiä
			 * voiTallentaa = false; */
			
			else if (currentText.startsWith("====")) {
				voiTallentaa = false;
			}
			//Jos voiTallentaa = true, lisätään se käännöslistaan
			if (voiTallentaa) {
				kaannoksetLista.add(currentText);
			}
			//Jatketaan seuraavalle riville
			luettavaRivi = br.readLine();
		}
		
		//Luodaan iteraattori, jolla käydä käännöslista läpi
		Iterator<String> itr = kaannoksetLista.iterator();
		while (itr.hasNext()) {
			String rivi = itr.next();
			
			/* Jos rivi alkaa #:lla ja rivi ei ala '#:':lla, niin
			 * rivi käsitellään modifioiRivi metodilla. Jos rivin sisältöä
			 * ei vielä ollut strinbuilderissa, lisätään se.
			 * Ensimmäinen ehto perustuu wiktionary:n apiin: kaikki 
			 * käännös matchit löytyvät merkkijonossa, jonka ensimmäinen merkki
			 * on #, puolestaan #: alkavat merkkijonot ovat vain esimerkkejä
			 * käännöksestä, eivät siis mielenkiintoisia käännöksen kannalta */
			
			if (rivi.startsWith("#") && !rivi.startsWith("#:")) {
				rivi = this.modifioiRivi(rivi);
				if (!sb.toString().contains(rivi)) {
					sb.append(rivi);
				}
			}
		}
		
		/* Jos stringbuilderin jono päättyy pilkkuun, poistetaan pilkku 
		 * ottamalla substring jonosta jättämällä viimeinen merkkipois(pilkku)
		 * ja palautetaan merkkijono */
		
		if (sb.toString().endsWith(",")) {
			return sb.substring(0, sb.toString().length() - 1);
		}
		//palauta stringbuilder merkkijonona
		return sb.toString().trim();
	}

	private String kaannoksetSuomeksi(String url) throws IOException {
		
		/* Metodi käytännössä sama kuin kaannoksetEnglanniksi
		 * tämän vuoksi metodia ei selitetä niin tarkasti. */
		
		ArrayList<String> kaannoksetLista = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.connect(url).get();
		boolean voiTallentaa = false;
		//luodaan br jolla rivit käydään läpi
		BufferedReader br = new BufferedReader(new StringReader(doc.toString()));
		String luettavaRivi = br.readLine();
		//käydään rivit läpi
		while (luettavaRivi != null) {
			//parsetaan jokaisesta rivistä html tagit pois
			String currentText = Jsoup.parse(luettavaRivi).text().toString();
			
			/* fi.wiktionary:ssa on mahdollista, että ==Englanti== ylätopicin
			 * lisäksi, luokitellaan kaikki =en= alle. jos match löytyy
			 * voiTallentaa = true
			 * */
			
			if (currentText.matches("(?i).*==Englanti==.*")
					|| currentText.matches("(?i).*=en=.*")) {
				voiTallentaa = true;
			} 
			
			/* Myös tässä kohdassa on eroavaisuuksia. fi.wiktionaryssa
			 * on mahdollista, että kappale ei pääty neljään = merkkiin,
			 * vaan alkaa suoraan ==X merkillä*/
			
			else if (currentText.startsWith("====")
					|| (currentText.startsWith("==") && Character
							.isUpperCase(currentText.charAt(2)))) {
				voiTallentaa = false;
			}
			//jos voi tallentaa, listätään kaannoksetListaan currentText
			if (voiTallentaa) {
				kaannoksetLista.add(currentText);
			}
			//luetaan uusi rivi
			luettavaRivi = br.readLine();
		}
		/* konstruktoidaan merkkijono, kuten edellisessä 
		 * metodissa selitetty */
		Iterator<String> itr = kaannoksetLista.iterator();
		while (itr.hasNext()) {
			String rivi = itr.next();
			if (rivi.startsWith("#") && !rivi.startsWith("#:")) {
				rivi = this.modifioiRivi(rivi);
				if (!sb.toString().contains(rivi)) {
					sb.append(rivi);
				}
			}
		}
		if (sb.toString().endsWith(",")) {
			return sb.substring(0, sb.toString().length() - 1);
		}
		return sb.toString().trim();
	}

	/**
	 * Metodi poistaa sulut, ja sanat sulkujen välistä, 
	 * jos sellaisia esiintyy merkkijonossa
	 * @param str käsiteltävä merkkijono
	 * @return merkkijono ilman sulkuja(ja niitten sisältöä)
	 */
	private String leikkaaSulut(String str) {
		//splitataan merkkijono
		String[] sanat = str.split(" ");
		ArrayList<String> lauseLista = new ArrayList<String>();
		int x = 0;
		//lisätään listaan jokainen taulukon sanat alkio
		while (x < sanat.length) {
			lauseLista.add(sanat[x]);
			x++;
		}
		//luodaan stringbuilderi
		StringBuilder builder = new StringBuilder();
		boolean onPoistettava = false;
		Iterator<String> tutkitaanSanat = lauseLista.iterator();
		//iteroidaan lauseLista
		while (tutkitaanSanat.hasNext()) {
			
			/* Toimii periaatteella, jos sanassa on sulut molemmilla puolilla,
			 * niin ei poisteta. Jos sulku vain toisella puoella, poistetaan
			 * kaikki sanat kahden sulun välistä */
			
			String tutkittavaSana = tutkitaanSanat.next();
			if (tutkittavaSana.startsWith("(") && tutkittavaSana.endsWith(")")) {
				onPoistettava = false;
			} else if (tutkittavaSana.startsWith("(")) {
				onPoistettava = true;
			} else if (tutkittavaSana.endsWith(")")) {
				tutkitaanSanat.remove();
				onPoistettava = false;
			}
			//jos onPoistettava = true, niin poistetaan tarkasteltava sana
			if (onPoistettava) {
				tutkitaanSanat.remove();
			}
		}
		Iterator<String> itr = lauseLista.iterator();
		//luoadan lopuksi uusi merkkijono ilman sulullisia sanoja
		while (itr.hasNext()) {
			builder.append(itr.next()).append(' ');
		}
		//palautetaan builder merkkijonona
		return builder.toString();
	}
/**
 * Metodilla muokataan jokainen rivi järkevään muotoon, jotta se voidaan 
 * tulostaa kelpaavassa muodossa käännöksineen
 * @param str käsiteltävä merkkijono
 * @return merkkijono modifioituna tulostettavaan muotoon
 */
	private String modifioiRivi(String str) {
		
		/* Alkuun korvataan tyhjällä merkillä, [,],~,+,# 
		 * tämän lisäksi . ja ; korvataan pilkulla */
		
		str = str.replace("[", "").replace("]", "").replace(".", ",")
				.replace("#", "").replace(";", ",").replace("~", "")
				.replace("+", "");
		
		//alkuun leikataan sulut pois
		str = this.leikkaaSulut(str);
		//luodaan sanataulukko ja stringbuilder
		StringBuilder sb = new StringBuilder();
		String[] sanaTaulukko = str.split(" ");
		
		//käydään taulukko läpi
		for (int i = 0; i < sanaTaulukko.length; i++) {
			
			/* jos sanassa ei ole kiinni {, ', #, }. |, niin 
			 * sana lisätään stringbuilderiin pienillä kirjaimilla */
			
			if (!sanaTaulukko[i].contains("{")
					&& !sanaTaulukko[i].contains("'")
					&& !sanaTaulukko[i].equals("#")
					&& !sanaTaulukko[i].contains("}")
					&& !sanaTaulukko[i].contains("|")) {
				if (!sb.toString().contains(sanaTaulukko[i])) {
					sb.append(' ').append(sanaTaulukko[i].toLowerCase());
				}
				//jos merkkijono sisältää |
			} else if (sanaTaulukko[i].contains("|")) {
				String[] sanaSplit = sanaTaulukko[i].split("|");
				//jos splittaamalla | kohtaa syntyy kaksi eri sanaa
				if (sanaSplit.length == 2 && sanaSplit[0] == sanaSplit[1]) {
					//jos sanat ovat identtiset, lisätään vain toinen niistä
					sb.append(' ').append(sanaSplit[0]);
				} else if (sanaSplit.length == 2
						&& sanaSplit[0] != sanaSplit[1]) {
					//muuten lisätään molemmat
					sb.append(' ').append(sanaSplit[0]).append(", ")
							.append(sanaSplit[1]);
				}
			}
		}
		//jos stringbuilder ei pääty pilkkuun, lisätään se
		if (!sb.toString().endsWith(",")) {
			sb.append(',');
		}
		
		//palautetaan stringbuilder merkkijonona
		return sb.toString();
	}
/**
 * Metodilla toteutetaan abstraktiluokka Vastaajan vaatima metodi.
 * @return merkkijononan vastaus
 */
	public String generoiVastaus(String viesti, String lahettaja) {
		try {
			/* jos viestissä on yli yksi sana, niin voidaan toteuttaa
			 * sillä tällöin viestissä löytyy haluttu komento(muuten
			 * ei olisi tullut Vastaajalta tälle)
			 *  */
			if (viesti.split(" ").length > 1) {
				//Jos haeKaannokset palauttaa jonkun merkkijonon
				if (this.haeKaannokset(viesti.split(" ")[0],
						viesti.split(" ")[1]).length() > 0) {
					/*jos sanan alkuosa on !fin ja komento on beer, niin
					 * lisätään että määritelmä voi myös tarkoittaa bönthöä
					 * ja palautetaan se */
					if (viesti.split(" ")[0].equals("!fin")
							&& viesti.split(" ")[1].equals("beer")) {
						return this.haeKaannokset(viesti.split(" ")[0],
								viesti.split(" ")[1])
								+ ", bönthö".trim();
					}
					//palautetaan haeKaannokset määräämä merkkijono
					return this.haeKaannokset(viesti.split(" ")[0],
							viesti.split(" ")[1]).trim();
				}
				//jos ei löydetty käännöstä eli merkkijono on tyhjä
				return "sanaa ei löytynyt";
			}
			//jos muuten vaan homma ei mennyt puikkoihin
			return null;
			//perus IOException catching
		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}
}
