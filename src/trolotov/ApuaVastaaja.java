package trolotov;
/**
 * ApuaVastaaja palauttaa muistutukseksi mieleen kaikki botin tuntemat komennot
 * :)
 * 
 * @author OLO3
 *
 */
public class ApuaVastaaja extends Vastaaja {
	/**
	 * Määritellään konstruktorissa botin reaktiokomennot
	 */
	public ApuaVastaaja() {
		this.reaktioKomennot = new String[] {"!apua", "!komennot", "!help"};
	}
	/**
	 * Toteutetaan Vastaaja luokan abstrakti metodi generoiVastaus
	 * @return merkkijono kaikista käytössä olevista komennoista
	 */
	public String generoiVastaus(String viesti, String lahettaja) {
		String[] komennot = {"!fakta", 
				"!tallennavastaus tuta/matikka vastaukset", 
				"!kerrovastaus tuta/matikka (tallentajan_nick)", 
				"!fin sana_englanniksi", "!eng sana_suomeksi", 
				"!reitti lähtöpaikka , tavoitepaikka", "!aika", 
				"!naamakerroin"};
		StringBuilder sb = new StringBuilder();
		sb.append('|').append(' ');
		for (int i = 0; i < komennot.length; i++) {
			sb.append(komennot[i]).append(' ').append('|').append(' ');
		}
		return sb.toString().trim();
	}
}
