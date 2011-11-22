package trolotov;
import java.util.Random;

/**
 * Kohu-Pete valitti ettei botti puhu sen kanssa. Tässä KP:lle nyt vastauksia.
 * 
 * @author HV
 * 
 */

public class BuubbeliVastaaja extends Vastaaja {
	private String[] vastaukset = { "IMACE!!!", "Sillä tavalla Petri", "Trolololololo",
			"<3<3<3<3", "EVERLASTIA!" , ":E", "etkä pökkää..."};
	private Random i = new Random();

	@Override
	public boolean viestiKiinnostaa(String Viesti, String lahettaja) {

		if (lahettaja.contains("Buubbeli") && i.nextDouble() <= 0.05) {
			return true;
		}
		return false;
	}

	public String generoiVastaus(String viesti, String lahettaja) {
		if (i.nextDouble() <= 0.5) {
				return this.vastaukset[i.nextInt(this.vastaukset.length)];
			}
			return viesti;
		}
	}