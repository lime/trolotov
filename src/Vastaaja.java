/**
 * Vastaaja-yläluokka. Tästä periytyvien luokkien pitää osata generoida fiksuja
 * vastauksia kun niille syötetään viesti.
 * 
 * @author Emil
 * 
 */
public abstract class Vastaaja {

	/**
	 * @param viesti
	 *            Toisen käyttäjän kirjoittama viesti.
	 * @param lahettaja
	 *            Viestin lähettäjän nimi.
	 * @return Vastausta jota generoidaan lähetettäväksi kanavaan. Jos mitään
	 *         fiksua ei keksitä voidaan palauttaa <code>null</code>.
	 */
	public abstract String generoiVastaus(String viesti, String lahettaja);

	private boolean tallennaTiedot(Object tiedot, String avainsana) {

	}
	
	private Object haeTiedot() {
		
	}

}
