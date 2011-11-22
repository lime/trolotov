package trolotov;
import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;


/** Käynnistää itse IRCbotin ja laittaa sen liittymään kanavalle.
 * @author lime
 *
 */
public class IRCbottiMain {

	/** Trolotovin 
	 * @param args parametrit
	 */
	public static void main(String[] args) {
		
		//Luodaan se
		IRCbotti trolotov = new IRCbotti("Trolotov");

		//debug päälle
		trolotov.setVerbose(true);
		
		//yhdistä
		try {
			trolotov.connect("irc.cs.hut.fi");
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//joinaa
		trolotov.joinChannel("#olotov");
	}

}
