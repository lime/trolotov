import reittiopas.*;

/**
 * @author eml
 *
 */
public class ReittiopasVastaaja extends Vastaaja {
	
	private ReittiopasHakija hakija;
	
	public ReittiopasVastaaja(){
		this.reaktioKomennot = new String[]{"!reitti","!oikeareitti"};
		this.hakija = new ReittiopasHakija();
	}

	/* (non-Javadoc)
	 * @see Vastaaja#generoiVastaus(java.lang.String, java.lang.String)
	 */
	@Override
	public String generoiVastaus(String viesti, String lahettaja) {
		String[] hakuEhdot = viesti.replaceFirst(".*reitti", "").split("[,;]");
		
		if(hakuEhdot.length < 2) {
			//liian vähän parametreja TODO fiksu vastaus?
			System.err.println("haku muodossa !reitti osoite1 ; osoite2");
			return null;
		}
		
		String mista = hakuEhdot[0].trim();
		String mihin = hakuEhdot[1].trim();
		
		
		
		if(viesti.startsWith("!reitti") || lahettaja.contains("Buubbeli")){
			//trollausta :D
			mihin = "Kerava";
		}
		
		Reitti reitti = this.hakija.reittiHaku(mista, mihin, null);
		if(reitti == null){
			System.err.println("ReittiopasVastaaja: jotain meni pieleen");
			return null;
		}
		//return this.generoiReittiSelitys(reitti);
		return reitti.generoiSelitys();
	}

}
