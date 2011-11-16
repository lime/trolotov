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

	public Delegoija() {
		RAND = new Random();

		this.vastaajat = new Vastaaja[] { new AikaVastaaja() /*
															 * , new
															 * ReittiopasVastaaja
															 * (), new
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

		String komento;
		if (viesti.split(" ").length == 0) {
			return null;
		} else {
			// jos viestissä on ainakin yksi sana
			komento = viesti.split(" ")[0];
		}

		/*
		 * Käy läpi vastaajat ja niiden komennot. Jos löytyy matchi niin
		 */
		for (Vastaaja vastaaja : this.vastaajat) {
			if (vastaaja.annaReaktioKommennot() != null) {
				for (String reaktioKomento : vastaaja.annaReaktioKommennot()) {
					if (komento.equalsIgnoreCase(reaktioKomento)) {
						vastaus = vastaaja.generoiVastaus(viesti, lahettaja);

						if (vastaus != null) {
							return vastaus;
						}
					}
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
