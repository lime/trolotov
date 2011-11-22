import java.util.Calendar;

/**
 * Kertoo nykyisen ajan.
 * 
 * @author Senja
 * 
 */
public class AikaVastaaja extends Vastaaja {
	/**
	 * Luo uuden aikavastaajan. Ainoa komento on <code>!aika</code>.
	 */
	public AikaVastaaja() {
		this.reaktioKomennot = new String[] { "!aika" };
	}

	public String generoiVastaus(String viesti, String lahettaja) {
		Calendar paiva = Calendar.getInstance();

		return "siis sä et niinQ tosiaan tiiä mikä päivä tänään on?!?!11"
				+ " no mäpä kerron sulle: tänään on "
				+ this.muutaSiistiksiPaivaksi(paiva);
	}

	/**
	 * Muuttaa kalenteripäivän hienoon tekstimuotoon.
	 * 
	 * @param paiva
	 *            Kyseinen päivä.
	 * @return Teinimuotoinen päivä-merkkijono.
	 */
	private String muutaSiistiksiPaivaksi(Calendar paiva) {
		int viikonPaiva = paiva.get(Calendar.DAY_OF_WEEK);
		String viikonPV = null;
		if (viikonPaiva == 1) {
			viikonPV = "sunnuntai";
		} else if (viikonPaiva == 2) {
			viikonPV = "VITTU MAANANTAI";
		} else if (viikonPaiva == 3) {
			viikonPV = "tiistai";
		} else if (viikonPaiva == 4) {
			viikonPV = "keskiviikko";
		} else if (viikonPaiva == 5) {
			viikonPV = "torstai";
		} else if (viikonPaiva == 6) {
			viikonPV = "JEE PERJANTAI";
		} else if (viikonPaiva == 7) {
			viikonPV = "lauantai";
		}
		int moneskoPaiva = paiva.get(Calendar.DAY_OF_MONTH);
		int kuukausi = paiva.get(Calendar.MONTH);
		String kuuta = null;
		if (kuukausi == 0) {
			kuuta = "tammiQta";
		} else if (kuukausi == 1) {
			kuuta = "helmiQta";
		} else if (kuukausi == 2) {
			kuuta = "maalisQta";
		} else if (kuukausi == 3) {
			kuuta = "huhtiQta";
		} else if (kuukausi == 4) {
			kuuta = "toukoQta";
		} else if (kuukausi == 5) {
			kuuta = "kesäQta";
		} else if (kuukausi == 6) {
			kuuta = "heinäQta";
		} else if (kuukausi == 7) {
			kuuta = "eloQta";
		} else if (kuukausi == 8) {
			kuuta = "syysQta";
		} else if (kuukausi == 9) {
			kuuta = "lokaQta";
		} else if (kuukausi == 10) {
			kuuta = "marrasQta";
		} else if (kuukausi == 11) {
			kuuta = "jouluQta";
		}
		int vuonna = paiva.get(Calendar.YEAR);
		int tunti = paiva.get(Calendar.HOUR);
		int minuutit = paiva.get(Calendar.MINUTE);
		return viikonPV + " " + moneskoPaiva + ". " + kuuta + " " + vuonna
				+ ". kello on niinQ " + tunti + ":" + minuutit;
	}
}
