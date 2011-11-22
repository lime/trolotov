package trolotov;

import java.util.Random;

import org.jibble.pircbot.User;

/**
 * 
 */

/**
 * @author eml
 * 
 */
public class Delegoija {

	private final Random RAND;

	private Vastaaja[] vastaajat;

	private Teini teini;

	// private TeiniVastaaja teini;

	/**
	 * Muistakaa lisätä vastaajat
	 */
	public Delegoija() {
		RAND = new Random();

		this.vastaajat = new Vastaaja[] { new AikaVastaaja(),
				new BuubbeliVastaaja(), new ReittiopasVastaaja(),
				new KaantajaVastaaja(), new LaskariVastaaja(),
				new LaskariTallentaja(), new NaamakerroinVastaaja(),
				new WikipediaVastaaja() };

		this.teini = new Teini();
	}

	/**
	 * Käsittelee uuden viestin ja palauttaa vastauksen.
	 * 
	 * @param viesti
	 *            Vastaanotettu viesti.
	 * @param lahettaja
	 *            Viestin lähettäjä.
	 * @return 'Fiksu' vastaus.
	 */
	public String kasitteleViesti(String viesti, String lahettaja) {
		String vastaus = null;

		// character encodingit kuntoon
		viesti = EncodingKaantaja.ISOtoUTF(viesti);
		System.out.println("Delegoija.kasitteleViesti() viesti: " + viesti);

		/*
		 * Käy läpi vastaajat ja niiden komennot. Jos löytyy matchi niin
		 */
		for (Vastaaja vastaaja : this.vastaajat) {
			if (vastaaja.viestiKiinnostaa(viesti, lahettaja)) {
				// kiinnostaa, generoi vastaus!
				vastaus = vastaaja.generoiVastaus(viesti, lahettaja);

				// palauta vaan jos tuli vastaus
				if (vastaus != null) {
					return Teini.muutaTeinix(vastaus); // TODO testaa
				}
			}

		}

		// viimeisenä annetaan teinivastaajalle, mutta vain esim 10% todnäk
		if (RAND.nextInt(100) < 10) {
			vastaus = teini.generoiVastaus(viesti, lahettaja);
		}

		// voi olla myös null jos ei kukaan keksiyt fiksua vastausta
		// muuttaa teinix
		return Teini.muutaTeinix(vastaus);
	}

	/**
	 * Käsittelee yksityisviestiä ihan samalla tavalla kuin kanavaviestiä.
	 * 
	 * @param viesti
	 *            Vastaanotettu viesti.
	 * @param lahettaja
	 *            Viestin lähettäjä.
	 * @return yksityisvastaus
	 */
	public String kasitteleYksityisViesti(String viesti, String lahettaja) {
		return this.kasitteleViesti(viesti, lahettaja);
		// TODO
	}

	/**
	 * Käsittelee botin liittymistä kanavalle laskemalla naamakertoimen kaikille
	 * kanavalla oleville.
	 * 
	 * @param kanava
	 *            IRC-kanava
	 * @param irkkaajat
	 *            kanavan käyttäjät
	 * @return onnistuneen luomisen hurraahuuto
	 */
	public String kasitteleBotinLiittyminen(String kanava, User[] irkkaajat) {
		for (int i = 0; i < irkkaajat.length; i++) {
			NaamakerroinLaskija.luoNaamakerroin(irkkaajat[i].getNick());
		}
		return "jee, kertoimet luotu";
	}

	/**
	 * Käsittelee käyttäjän liittymistä kanavalle laskemalla naamakertoimen
	 * sille.
	 * 
	 * @param kanava
	 *            IRC-kanava
	 * @param lahettaja
	 *            Tyyppi joka liittyy
	 * @param login
	 *            Käyttäjän login
	 * @param hostname
	 *            Käyttäjän hostname
	 * @return onnistuneen luomisen hurraahuuto
	 */
	public String kasitteleIrkkaajanLiittyminen(String kanava,
			String lahettaja, String login, String hostname) {
		if (!NaamakerroinLaskija.onkoJoNaamakerroin(lahettaja)) {
			NaamakerroinLaskija.luoNaamakerroin(lahettaja);
		}
		return "jeejee, naamakerroin luotu";
	}

	/**
	 * Käsittelee käyttäjän poistumista kanavalta poistamalla naamakertoimen
	 * siltä.
	 * 
	 * @param kanava
	 *            IRC-kanava
	 * @param lahettaja
	 *            Tyyppi joka poistuu
	 * @param login
	 *            Käyttäjän login
	 * @param hostname
	 *            Käyttäjän hostname
	 */
	public void kasitteleIrkkaajanPoistuminen(String kanava, String lahettaja,
			String login, String hostname) {
		if (NaamakerroinLaskija.onkoJoNaamakerroin(lahettaja)) {
			NaamakerroinLaskija.poistaNaamakerroin(lahettaja);
		}
	}

	/**
	 * Käsittelee käyttäjän nickin muuttumisen laittamalla muistiin kenen nicki
	 * muuttui ja miten. Niin helposti ei kerran saatu naamakerroin katoa. :)
	 * 
	 * @param vanhaNick
	 *            Käyttäjän entinen nick.
	 * @param login
	 *            Käyttäjän login
	 * @param hostname
	 *            Käyttäjän hostname
	 * @param uusiNick
	 *            Käyttäjän uusi nick.
	 */
	public void kasitteleIrkkaajanNickinMuuttuminen(String vanhaNick,
			String login, String hostname, String uusiNick) {
		NaamakerroinLaskija.muutaNimea(vanhaNick, uusiNick);
	}

}
