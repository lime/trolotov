/**
 * 
 */
package reittiopas;

import java.util.ListIterator;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * @author eml
 *
 */
public class Reitti {
	
	private Element reittiOhje;

	public Reitti(Element reittiOhje) {
		this.reittiOhje = reittiOhje;
	}

	/** Tekee "fiksun" selityksen reitistä.
	 * @return
	 */
	public String generoiSelitys() {
		System.out.println("Reitti.generoiSelitys()" + this.reittiOhje.tagName());
		System.err.println(this.reittiOhje.children().size());
		
		
		String selitys = "From "+this.reittiOhje.attr("from")+" to "+this.reittiOhje.attr("to")+": ";
		
		ListIterator<Element> matkanOsat = this.reittiOhje.children().listIterator();
		while(matkanOsat.hasNext()) {
			Element osaMatka = matkanOsat.next();
			
			//TODO käsittele walk ja line
			if(osaMatka.tagName().equalsIgnoreCase("WALK")){
				selitys = selitys.concat("kävele paikkaan "+"X"+", ");//TODO
			} else if (osaMatka.tagName().equalsIgnoreCase("LINE")) {
				selitys = selitys.concat("ota linja "+"Y"+" paikkaan "+"Z"+" ja ");//TODO
			}
			//DEBUG selitys = selitys.concat(" "+osaMatka.tagName());
		}
		
		return selitys;		
	}

}
