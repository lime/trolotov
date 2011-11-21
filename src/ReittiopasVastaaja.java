import reittiopas.*;

/**
 * @author eml
 * 
 */
public class ReittiopasVastaaja extends Vastaaja {

	private ReittiopasHakija hakija;

	public ReittiopasVastaaja() {
		this.reaktioKomennot = new String[] { "!reitti", "!oikeareitti" };
		this.hakija = new ReittiopasHakija();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Vastaaja#generoiVastaus(java.lang.String, java.lang.String)
	 */
	@Override
	public String generoiVastaus(String viesti, String lahettaja) {
		String[] hakuEhdot = viesti.replaceFirst(".*reitti", "").split("[,;]");

		if (hakuEhdot.length < 2) {
			// liian vähän parametreja TODO fiksu vastaus?
			return "Hae muodossa !reitti osoite1 ; osoite2";
		}

		String mista = hakuEhdot[0].trim();
		String mihin = hakuEhdot[1].trim();

		if (viesti.startsWith("!reitti") || lahettaja.contains("Buubbeli")) {
			// trollausta :D
			mihin = "Kerava";
		}

		Reitti reitti = this.hakija.reittiHaku(mista, mihin, null);

		if (reitti == null) {
			// epäonnistui
			return "Paskat osoitteet sulla, en mä tota ymmärrä. :D:D";
		}

		return reitti.generoiSelitys();
	}

}
