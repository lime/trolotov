import java.util.HashMap;

/**
 * 
 */

/**
 * @author lime
 * 
 */
public class NaamakerroinLaskija {

	private static HashMap<String, Double> naamakertoimet;

	static {
		naamakertoimet = new HashMap<String, Double>();

		naamakertoimet.put("Buubbeli", 2.0);
	}

	/**Kertoo käyttäjän naamakertoimen
	 * @param nimi Käyttäjän nick
	 * @return Naamakerroin (välistä [0,10])
	 */
	public int annaNaamakerroin(String nimi) {
		if (naamakertoimet.containsKey(nimi)) {
			return (int) Math.floor(naamakertoimet.get(nimi));
		} else {
			//TODO -1 tällä hetkellä
			return -1;
		}
	}

}
