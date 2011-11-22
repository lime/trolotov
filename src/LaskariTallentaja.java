import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/** 
 * LaskariTallentaja huolehtii syötettyjen laskarivastauksien tallentamisesta.
 * @author Mikko Latva-Käyrä
 *
 */

public class LaskariTallentaja extends Vastaaja {

	public LaskariTallentaja(){
		this.reaktioKomennot = new String[] { "!tallennavastaus" };
	}
	/** 
	 * Metodi tallentaa kommentin, jossa on oikeat avainsanat (!tallennavastaus 
	 * tuta/matikka) alussa, tekstitiedostoon vastaukset.txt. Tutan vastaukset
	 * merkataan $-merkillä ja matikan vastaukset # merkillä. Sen lisäksi
	 * rivin loppuun lisätään vielä, kuka kyseiset vastaukset on tallentanut.
	 * @param kiinnostavaLause
	 * lähettäjän kokonaisuudessaan lähettämä kommentti
	 * @param lahettaja
	 * viestin lähettäjä, merkitään tekstitiedostoon
	 * @throws IOException
	 * BufferedReader heittää IOExceptionin, joka pitää käsitellä
	 */
	public void tallennaVastausTiedostoon(String kiinnostavaLause, String lahettaja)
			throws IOException {
		String osat[] = kiinnostavaLause.split(" ");
		try {
			BufferedWriter kirjoittaja = new BufferedWriter(
					new FileWriter("vastaukset.txt", true));

			/*System.out.println(kiinnostavaLause.substring(osat[0].length() + 
			 * osat[1].length() + 1).trim()); */

			if (osat[1].equals("tuta")){
				kirjoittaja.newLine();
				kirjoittaja.write("$" + " " + "\"" + kiinnostavaLause.
						substring(osat[0].length() + osat[1].length() + 1).trim()
						+ "\" kirjoittanut " + lahettaja);
				kirjoittaja.close( );

			}

			if (osat[1].equals("matikka")){
				kirjoittaja.newLine();
				kirjoittaja.write("#" + " " + "\"" + kiinnostavaLause.
						substring(osat[0].length() + osat[1].length() + 1).trim()
						+ "\" kirjoittanut " + lahettaja);
				kirjoittaja.close( );
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * Muodostaa vastauksen käyttämällä metodia tallennaVastausTiedostoon, ja
	 * ja palauttaa sen. IOException pitää catchata.
	 */
	public String generoiVastaus(String viesti, String lahettaja) {
		try {
			this.tallennaVastausTiedostoon(viesti, lahettaja);
			return "Vastauksesi tallennetiin onnistuneesti.";
		}
		catch (IOException e){
			e.printStackTrace();
			return "Vastauksen tallentaminen ei onnistunut.";
		}
	}

	/*public static void main(String[] args) throws IOException {
		LaskariTallentaja tallentaja = new LaskariTallentaja();
		System.out.println(tallentaja.generoiVastaus("!tallennavastaus tuta 1. plaplaa 2. pla", "HV"));
		System.out.println(tallentaja.generoiVastaus("!tallennavastaus matikka 1. plaa 2. plapla", "HV"));
		System.out.println(tallentaja.generoiVastaus("!tallennavastaus tuta 1. pladadaplaa 2. pal", "HV"));
	}*/
}
