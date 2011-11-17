import java.util.Random;

/**
 * 
 */

/**
 * @author lime
 * 
 */
public class Delegoija {

	private final Random RAND;

	private Vastaaja[] vastaajat;

	// private TeiniVastaaja teini;

	/**
	 * Muistakaa lisätä vastaajat
	 */
	public Delegoija() {
		RAND = new Random();

		this.vastaajat = new Vastaaja[] { new AikaVastaaja(),
				new BuubbeliVastaaja() /*
										 * new ReittiopasVastaaja (), new
										 * LaskariVastaaja()
										 */
		};

		// this.teini = new TeiniVastaaja();
	}

	/**
	 * @param viesti
	 * @return
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
					return vastaus;
				}
			}

		}

		// viimeisenä annetaan teinivastaajalle, mutta vain esim 20% todnäk
		if (RAND.nextInt(100) < 20) {
			// vastaus = teini.generoiVastaus(viesti, lahettaja);
		}

		// voi olla myös null jos ei kukaan keksiyt fiksua vastausta
		return vastaus;
	}

	/**
	 * @param lahettaja
	 * @param viesti
	 */
	public String kasitteleYksityisViesti(String viesti, String lahettaja) {
		// TODO Auto-generated method stub
		return null;

	}

}
