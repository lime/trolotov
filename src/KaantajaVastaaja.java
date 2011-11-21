import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class KaantajaVastaaja extends Vastaaja {
	public KaantajaVastaaja() {
		this.reaktioKomennot = new String[] {"!fin","!eng"};
	}
	public String haeKaannokset(String komento, String sana) throws IOException {
		String url;
		if (komento.equals("!eng")) {
			url = "http://en.wiktionary.org/w/api.php?action=query&prop=revisions|lllang=fi&titles=" + sana + "&rvprop=content";
			return this.kaannoksetEnglanniksi(url);
		}
		else if (komento.equals("!fin")) {
			url = "http://fi.wiktionary.org/w/api.php?action=query&prop=revisions|lllang=fi&titles=" + sana + "&rvprop=content";
			return this.kaannoksetSuomeksi(url);
		}
		return null;
	}
		public String kaannoksetEnglanniksi(String url) throws IOException {  	
			ArrayList<String> kaannoksetLista = new ArrayList<String>();
			ArrayList<String> kasitellytSanat = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			Document doc = Jsoup.connect(url).get();
			boolean voiTallentaa = false;
			BufferedReader br = new BufferedReader(new StringReader(doc.toString()));
			String luettavaRivi = br.readLine();
			while (luettavaRivi != null) {
				String currentText = Jsoup.parse(luettavaRivi).text().toString();
				if (currentText.matches("(?i).*==Finnish==.*")) {
					voiTallentaa = true;
				}
				else if (currentText.startsWith("====")) {
					voiTallentaa = false;
				}
				if (voiTallentaa) {
					kaannoksetLista.add(currentText);
				}
				luettavaRivi = br.readLine();
			}
			Iterator<String> itr = kaannoksetLista.iterator();
			while (itr.hasNext()) {
				String rivi = itr.next();
				if (rivi.startsWith("#") && !rivi.startsWith("#:")) {
					rivi = this.modifioiRivi(rivi);
					if (!sb.toString().contains(rivi)) {
						sb.append(rivi);
					}
				}
			}
			if (sb.toString().endsWith(",")) {
				return sb.substring(0, sb.toString().length()-1);
			}
			return sb.toString().trim();
		}
		public String kaannoksetSuomeksi(String url) throws IOException {  	
			ArrayList<String> kaannoksetLista = new ArrayList<String>();
			ArrayList<String> kasitellytSanat = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			Document doc = Jsoup.connect(url).get();
			boolean voiTallentaa = false;
			BufferedReader br = new BufferedReader(new StringReader(doc.toString()));
			String luettavaRivi = br.readLine();
			while (luettavaRivi != null) {
				String currentText = Jsoup.parse(luettavaRivi).text().toString();
				//System.out.println(currentText);
				if (currentText.matches("(?i).*==Englanti==.*") || currentText.matches("(?i).*=en=.*")) {
					voiTallentaa = true;
				}
				else if (currentText.startsWith("====") || (currentText.startsWith("==") && Character.isUpperCase(currentText.charAt(2)))) {
					voiTallentaa = false;
				}
				if (voiTallentaa) {
					//System.out.println("Tallennetaan: " + currentText);
					kaannoksetLista.add(currentText);
				}
				luettavaRivi = br.readLine();
			}
			Iterator<String> itr = kaannoksetLista.iterator();
			while (itr.hasNext()) {
				String rivi = itr.next();
				if (rivi.startsWith("#") && !rivi.startsWith("#:")) {
					rivi = this.modifioiRivi(rivi);
					if (!sb.toString().contains(rivi)) {
						sb.append(rivi);
					}
				}
			}
			if (sb.toString().endsWith(",")) {
				return sb.substring(0, sb.toString().length()-1);
			}
			return sb.toString().trim();
		}
		
	public String leikkaaSulut(String str) {
		String[] sanat = str.split(" ");
		ArrayList<String> lauseLista = new ArrayList<String>();
		int x = 0;
		while (x < sanat.length) {
			lauseLista.add(sanat[x]);
			x++;
		}
		StringBuilder builder = new StringBuilder();
		boolean onPoistettava = false;
		Iterator<String> tutkitaanSanat = lauseLista.iterator();
		while(tutkitaanSanat.hasNext()) {
			String tutkittavaSana = tutkitaanSanat.next();
			if (tutkittavaSana.startsWith("(") && tutkittavaSana.endsWith(")")) {
				onPoistettava = false;
			}
			else if (tutkittavaSana.startsWith("(")) {
				onPoistettava = true;
			}
			else if (tutkittavaSana.endsWith(")")) {
				tutkitaanSanat.remove();
				onPoistettava = false;
			}
			if (onPoistettava) {
				tutkitaanSanat.remove();
			}
		}
		Iterator<String> itr = lauseLista.iterator();
		while (itr.hasNext()) {
			builder.append(itr.next()).append(' ');
		}
		return builder.toString();
	}

	public String modifioiRivi(String str) {
		str = str.replace("[", "").replace("]","").replace(".", ",").replace("#", "").replace(";", ",");
		str = this.leikkaaSulut(str);
		StringBuilder sb = new StringBuilder();
		String[] sanaTaulukko = str.split(" ");
		for (int i = 0; i < sanaTaulukko.length; i++) {
			if (!sanaTaulukko[i].contains("{") && !sanaTaulukko[i].contains("'") && !sanaTaulukko[i].equals("#") && !sanaTaulukko[i].contains("}") && !sanaTaulukko[i].contains("|")) {
				if (!sb.toString().contains(sanaTaulukko[i])) {
					sb.append(' ').append(sanaTaulukko[i].toLowerCase());
				}
			}
			else if (sanaTaulukko[i].contains("|")) {
				String[] sanaSplit = sanaTaulukko[i].split("|");
				if (sanaSplit.length == 2 && sanaSplit[0] == sanaSplit[1]) {
					sb.append(' ').append(sanaSplit[0]);
				}
				else if (sanaSplit.length == 2 && sanaSplit[0] != sanaSplit[1]) {
					sb.append(' ').append(sanaSplit[0]).append(", ").append(sanaSplit[1]);
				}
			}
		}
		if (!sb.toString().endsWith(",")) {
			sb.append(',');
		}
		return sb.toString();
	}

	public String poistaTagit(String str) {
		String[] sanaTaulukko = str.split(" ");
		StringBuilder sb = new StringBuilder();
		int x = 0;
		boolean onPoistettava = false;
		while (x < sanaTaulukko.length) {
			if (sanaTaulukko[x].startsWith("<")) {
				onPoistettava = true;
			}
			else if (sanaTaulukko[x].endsWith(">")) {
				sanaTaulukko[x] = null;
				onPoistettava = false;
			}
			if (onPoistettava) {
				sanaTaulukko[x] = null;
			}
			x++;
		}
		for (int i = 0; i < sanaTaulukko.length; i++) {
			if (sanaTaulukko[i] != null) {
				sb.append(sanaTaulukko[i]).append(' ');
			}
		}
		return sb.toString();
	}
	public String generoiVastaus(String viesti, String lahettaja) {
		try {
			if (viesti.split(" ").length > 1) {
				System.out.println(viesti.split(" ")[0]);
				System.out.println(viesti.split(" ")[1]);
				if (this.haeKaannokset(viesti.split(" ")[0], viesti.split(" ")[1]).length() > 0) {
					if (viesti.split(" ")[0].equals("!fin") && viesti.split(" ")[1].equals("beer")) {
						return this.haeKaannokset(viesti.split(" ")[0], viesti.split(" ")[1]) + ", b�nth�".trim();
					}
					return this.haeKaannokset(viesti.split(" ")[0], viesti.split(" ")[1]).trim();
					}
				return "sanaa ei l�ytynyt";
			}
			return null;
			}
		catch (IOException e) {
			e.printStackTrace();
			return null;

		}
	}
	public static void main(String[] args) throws IOException {
		KaantajaVastaaja k = new KaantajaVastaaja();
		System.out.println(k.generoiVastaus("!fin rum","cloud"));
		//String s = "[[avs]]";
		//System.out.println(s.substring(s.indexOf("[[")+2, s.indexOf("]]")));
	}
}
