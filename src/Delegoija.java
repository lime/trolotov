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
				new KaantajaVastaaja(),
				/* new LaskariVastaaja(), */new NaamakerroinVastaaja(),
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

		/*
		 * Käy läpi vastaajat ja niiden komennot. Jos löytyy matchi niin
		 */
		for (Vastaaja vastaaja : this.vastaajat) {
			if (vastaaja.viestiKiinnostaa(viesti, lahettaja)) {
				// kiinnostaa, generoi vastaus!
				vastaus = vastaaja.generoiVastaus(viesti, lahettaja);

				// palauta vaan jos tuli vastaus
				if (vastaus != null) {
					return Teini.muutaTeinix(vastaus); //TODO testaa
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

	public String kasitteleBotinLiittyminen(String kanava, User[] irkkaajat) {
		for (int i = 0; i < irkkaajat.length; i++) {
			NaamakerroinLaskija.luoNaamakerroin(irkkaajat[i].getNick());
		}
		return "jee, kertoimet luotu";
	}

	public String kasitteleIrkkaajanLiittyminen(String kanava,
			String lahettaja, String login, String hostname) {
		if (!NaamakerroinLaskija.onkoJoNaamakerroin(lahettaja)) {
			NaamakerroinLaskija.luoNaamakerroin(lahettaja);
		}
		return "jeejee, naamakerroin luotu";
	}

	public void kasitteleIrkkaajanPoistuminen(String kanava, String lahettaja,
			String login, String hostname) {
		if (NaamakerroinLaskija.onkoJoNaamakerroin(lahettaja)) {
			NaamakerroinLaskija.poistaNaamakerroin(lahettaja);
		}
	}

	public void kasitteleIrkkaajanNickinMuuttuminen(String vanhaNick,
			String login, String hostname, String uusiNick) {
		NaamakerroinLaskija.muutaNimea(vanhaNick, uusiNick);
	}

}
