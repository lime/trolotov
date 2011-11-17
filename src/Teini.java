import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Teini extends Vastaaja {

	Random rand = new Random();

	List<String> kivat;

	List<String> ilkeet;

	List<String> ranteetAuki;

	public Teini(){
		String[] kivatTaulukko = new String[]{
				"s� oot niin ihQ!!<3" , "siis love u 4ever!!!!1" , "<333333",
				"loveU 4ever", "s� oot mun bestis", "I<3U", "aaaawwwwwww", ";)"
		};
		kivat = Arrays.asList(kivatTaulukko);
		String[] ilkeetTaulukko = new String[]{
				"s� oot niin lame", "vittu m� hajoon suhun", "WTF!?! mit� s�" +
						" kelaat?!", "ROF LOL", "s� oot niin teinixpissix!",

		};
		ilkeet = Arrays.asList(ilkeetTaulukko);
		String[] aukiTaulukko = new String[]{
				"vittu mit� paskaa?!!!1", ""
		};
		ranteetAuki = Arrays.asList(aukiTaulukko);
	}

	@Override
	public boolean viestiKiinnostaa(String viesti, String lahettaja){
		return true;
	}

	public String generoiVastaus(String viesti, String lahettaja){
		double i = rand.nextInt();
		int naamakerroin = Naamakerroinlaskija.annaNaamakerroin(lahettaja);
		if (naamakerrroin > 8 && i > 0.5){
			return kivat.get(rand.nextInt(kivat.size()-1));
		}
		else if (naamakerroin <= 2 && i > 0.4){
			return ilkeet.get(rand.nextInt(ilkeet.size()-1));
		}
		else if (naamakerroin <= 8 && naamakerroin > 5){
			if (i > 0.9){
				return kivat.get(rand.nextInt(kivat.size()-1));
			}
			if (i < 0.03){
				return ilkeet.get(rand.nextInt(ilkeet.size()-1));
			}
		}
		else if (naamakerroin < 5 && naamakerroin > 2){
			if (i > 0.9){
				return ilkeet.get(rand.nextInt(ilkeet.size()-1));
			}
		}
		else {
			return ranteetAuki.get(rand.nextInt(ranteetAuki.size()-1));
		}

	}

	//Siis daa� lovex 4ever!!
	//Siis emm� mik�� n�rtti oo?
	//XDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
	//^^
	//Siis ku m� olin t�s (t�n��/yks maanantai/eile) (stadis/skolel/d�s�ril) ja (bostasin/hengasin/chillailin)(frendin/mutsin/faija/ yhen nevarin) kaa ni siis (XD/ LOL/ LOLOLLOL/ WTF?!?!) m� (tsiigasin/ tsekkasin) yht (heeboo/j�b��/tyyppii) ja siis (XD/ LOL/ LOLOLLOL/ OMG/ ihquu) se oli (vittu/vitu/prkl) (HV siis Henri Virtanen/ Cheek/ Antti Tuisku)!!!!
	//
	//S� oot niin lame, emm� sulle puhu!
	//
	//No siis vink vink, mit� sul ois illal pl��neis?
	//-> Meil ois niiku k�ty ;DDDDDDD
	//-> voitais pit�� pikku (bileet/juhlat/partyt/naamiaiskemut/lanisessiot/illalliset)� T�st tulee sairaan mageet!!
	//-> mit�s funtsit?
	//Se ois sit meil seiskan kieppeill� :DDDDDD
	//
	//Siiis awwwwwwwww�. 
}
