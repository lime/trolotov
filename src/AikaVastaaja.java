/**
 * @author HV
 * 
 */
public class AikaVastaaja extends Vastaaja {
	public AikaVastaaja() {
		this.reaktioKomennot = new String[] { "!aika" };
	}

	public String generoiVastaus(String viesti, String lahettaja) {
		String aika = new java.util.Date().toString();
		return aika;
	}
}
