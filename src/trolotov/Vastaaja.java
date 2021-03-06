package trolotov;
/**
 * Vastaaja-yläluokka. Tästä periytyvien luokkien pitää osata generoida fiksuja
 * vastauksia kun niille syötetään viesti.
 * 
 * @author Emil
 * 
 */
public abstract class Vastaaja {

	/**
	 * Ne kommenot joihin tämä Vastaaja reagoi. Pitää alustaa itse Vastaajan
	 * konstruktorissa!
	 */
	protected String[] reaktioKomennot;

	/**
	 * Tarkistaa kiinnostaako kyseinen viesti tätä vastaajaa. Defaultina se
	 * katsoo läpi reaktioKommennot-taulukkoa jos viesti alkaisi jollakin
	 * niistä. Tämän voi overrideata jos kiinnostus riippuu muista faktoreista,
	 * esim lähettäjästä tai satunnaisuudesta.
	 * 
	 * @param viesti
	 *            Lähetetty viesti
	 * @param lahettaja
	 *            Viestin lähettäjä
	 * @return Kertoo jos viesti merkitsee tälle vastaajalle mitään, eli
	 *         löytysikö sille ehkä vastaus.
	 */
	public boolean viestiKiinnostaa(String viesti, String lahettaja) {
		if (this.reaktioKomennot != null) {
			for (String reaktioKomento : this.reaktioKomennot) {
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

}
