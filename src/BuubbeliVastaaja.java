import java.util.Random;

/**
 * Kohu-Pete valitti ettei botti puhu sen kanssa. Tässä KP:lle nyt vastauksia.
 * 
 * @author HV
 * 
 */

public class BuubbeliVastaaja extends Vastaaja {
	String[] vastaukset = { "IMACE!!!", "Sillä tavalla Petri", "Trolololololo",
			"<3<3<3<3", "EVERLASTIA!" };
	private Random i = new Random();

	@Override
	public boolean viestiKiinnostaa(String Viesti, String lahettaja) {

		if (lahettaja.startsWith("Buubbeli")) {
			return true;
		}
		return false;
	}

	public String generoiVastaus(String viesti, String lahettaja) {
		if (i.nextInt(100) < 10) {
			return this.vastaukset[i.nextInt(this.vastaukset.length)];
		} else {
			return null;
		}
	}
}