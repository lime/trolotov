/**
 * Vastaaja-yläluokka. Tästä periytyvien luokkien pitää osata generoida fiksuja
 * vastauksia kun niille syötetään viesti.
 * 
 * @author Emil
 * 
 */
public abstract class Vastaaja {

	// TODO private Sihteeri sihteeri;
	/**
	 * Ne kommenot joihin tämä Vastaaja reagoi. Pitää alustaa itse Vastaajan
	 * konstruktorissa!
	 */
	protected String[] reaktioKommennot;

	/**
	 * @return the reaktioKommennot
	 */
	public String[] annaReaktioKommennot() {
		return this.reaktioKommennot;
	}

	/**
	 * @param viesti
	 *            Toisen käyttäjän kirjoittama viesti.
	 * @param lahettaja
	 *            Viestin lähettäjän nimi.
	 * @return Vastausta jota generoidaan lähetettäväksi kanavaan. Jos mitään
	 *         fiksua ei keksitä voidaan palauttaa <code>null</code>.
	 */
	public abstract String generoiVastaus(String viesti, String lahettaja);

	protected void tallennaTiedot(Object tiedot, String avainsana) {

	}

	protected void tallennaTiedotLevylle() {

	}

	protected Object haeTiedot() {
		return null;

	}

}
