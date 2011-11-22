import java.util.HashMap;

/**
 * @author Senja
 * 
 */
public class NaamakerroinLaskija {

	private static HashMap<String, Double> naamakertoimet;

	static {
		naamakertoimet = new HashMap<String, Double>();

	}
	
	//TODO k�yt� silloin kun joinataan
	/**
	 * Metodia on tarkoitus k�ytt��, kun bottii liittyy kanavalle ja k�y kaikki
	 * nickit l�pi
	 * TAI silloin kun uusi irkkaaja liittyy kanavalle
	 * TAI kun joku vaihtaa nicki�
	 * @param nimi Sen irkkaajan nick, jolle luodaan naamakerroin
	 */
	public static void luoNaamakerroin(String nimi){
		if(!naamakertoimet.containsKey(nimi)){
			if(nimi.equals("anQ") || nimi.equals("mevi")){
				naamakertoimet.put(nimi, 10.0);
			}
			else if(nimi.equals("Buubbeli")){
				naamakertoimet.put(nimi, 2.0);
			}
			else{
				double kerroin = Math.random()*10;
				naamakertoimet.put(nimi, kerroin);
			}
		}
	}

	/**Kertoo k�ytt�j�n naamakertoimen
	 * @param nimi K�ytt�j�n nick
	 * @return Naamakerroin (v�list� [0,10])
	 */
	public static int annaNaamakerroin(String nimi) {
		if (naamakertoimet.containsKey(nimi)) {
			return (int) Math.floor(naamakertoimet.get(nimi));
		} else {
			//TODO -1 t�ll� hetkell�
			return -1;
		}
	}
	
	/**
	 * Metodi korottaa irkkaajan naamakerrointa 0.1:lla
	 * 
	 * @param nimi Sen irkkaajan nick, jonka naamakerrointa halutaan korottaa
	 */
	public static void korotaNaamakerrointa(String nimi){
		if(naamakertoimet.containsKey(nimi)){
			double kerroin = naamakertoimet.get(nimi);
			kerroin = kerroin + 0.1;
			naamakertoimet.put(nimi, kerroin);
		}
	}
	
	/**
	 * Metodi alentaa irkkaajan naamakerrointa 0.1:lla
	 * 
	 * @param nimi Sen irkkaajan nick, jonka naamakerrointa halutaan alentaa
	 */
	public static void alennaNaamakerrointa(String nimi){
		if(naamakertoimet.containsKey(nimi)){
			double kerroin = naamakertoimet.get(nimi);
			kerroin = kerroin - 0.1;
			naamakertoimet.put(nimi, kerroin);
		}
	}

	public static boolean onkoJoNaamakerroin(String nimi){
		if(naamakertoimet.containsKey(nimi)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void poistaNaamakerroin(String nimi){
		if(naamakertoimet.containsKey(nimi)){
			naamakertoimet.remove(nimi);
		}
	}
	
	public static void muutaNimea(String vanhaNimi, String uusiNimi){
		double muistiArvo = naamakertoimet.get(vanhaNimi);
		naamakertoimet.remove(vanhaNimi);
		naamakertoimet.put(uusiNimi, muistiArvo);
	}
}
