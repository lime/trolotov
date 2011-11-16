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
	private /*Teini*/Vastaaja teini;

	/*
	 * private TeiniVastaaja teini; private ReittiopasVastaaja reittiopas;
	 * private LaskariVastaava laskari;
	 */

	/**
	 * 
	 */
	public Delegoija() {
		RAND = new Random();
		//
	}

	/**
	 * @param viesti
	 * @return
	 */
	public String kasitteleViesti(String viesti, String lahettaja) {
		String vastaus = null;

		String komento = viesti.split(" ")[0];

		/*
		 * Käy läpi vastaajat ja niiden komennot. Jos löytyy matchi niin
		 */
		for (Vastaaja vastaaja : vastaajat) {
			for (String reaktioKomento : vastaaja.annaReaktioKommennot()) {
				if (komento.equalsIgnoreCase(reaktioKomento)) {
					vastaus = vastaaja.generoiVastaus(viesti, lahettaja);
					
					if(vastaus != null) {
						return vastaus;
					}
				}
			}
		}

		// viimeisenä annetaan teinivastaajalle, mutta vain esim 20% todnäk
		if (RAND.nextInt(100) < 20) {
			vastaus = teini.generoiVastaus(viesti, lahettaja);
		}
		
		//voi olla myös null jos ei kukaan keksiyt fiksua vastausta
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
