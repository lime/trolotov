package trolotov;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Tarkoitettu botin vastaajien testaamiseen paikallisesti ilman että tarvitsee
 * yhdistää IRCiin.
 * 
 * @author eml
 * 
 */
public class VastaajaTestaaja {

	/**
	 * Käynnistää uuden testaajan.
	 * 
	 * @param args
	 *            Argumentit
	 */
	public static void main(String[] args) {

		Delegoija delegoija = new Delegoija();
		BufferedReader lukija = new BufferedReader(new InputStreamReader(
				System.in));

		String viesti = null, vastaus = null;
		for (int i = 0; i < 10; i++) {
			System.out.print("Koodaaja:\t");
			try {
				viesti = lukija.readLine();
				vastaus = delegoija.kasitteleViesti(viesti, "Koodaaja");
			} catch (IOException e) {
				System.err.println("paska viesti, uudestaan");
			}

			if (vastaus != null) {
				System.out.println("Trolotov:\t" + vastaus);
			}

		}

	}

}
