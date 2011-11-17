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
	 * Tarkistaa kiinnostaako kyseinen viesti tätä vastaajaa. Defaultina se
	 * katsoo läpi reaktioKommennot-taulukkoa jos viesti alkaisi jollakin
	 * niistä. Tämän voi overrideata jos kiinnostus riippuu muista faktoreista,
	 * esim lähettäjästä tai satunnaisuudesta.
	 * 
	 * @param viesti Lähetetty viesti
	 * @param lahettaja Viestin lähettäjä
	 * @return 
	 */
	public boolean viestiKiinnostaa(String viesti, String lahettaja) {
		if (this.reaktioKommennot != null) {
			for (String reaktioKomento : this.reaktioKommennot) {
				if (viesti.trim().toLowerCase()
						.startsWith(reaktioKomento.toLowerCase())) {
					return true;
				}
			}
		}

		return false;
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
