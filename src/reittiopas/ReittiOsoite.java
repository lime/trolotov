package reittiopas;
import org.jsoup.nodes.Element;

/**
 * 
 */

/**
 * @author eml
 *
 */
public class ReittiOsoite {
	
	private Element paikka;
	
	public ReittiOsoite(Element paikka) {
		if(paikka != null) {
			this.paikka = paikka;	
		}
	}
	
	public String annaX() {
		return this.paikka.attr("x");
	}
	
	public String annaY(){
		return this.paikka.attr("y");
	}
	
	public String annaNimi(){
		return this.paikka.attr("name1") + this.paikka.attr("number");
	}
	
	public String annaKaupunki(){
		return this.paikka.attr("city");
	}
	
	

}
