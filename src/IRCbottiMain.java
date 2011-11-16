import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

/**
 * 
 */

/**
 * @author lime
 *
 */
public class IRCbottiMain {

	public static void main(String[] args) {
		
		//Luodaan se
		IRCbotti trolotov = new IRCbotti("Trolotov");

		//debug päälle
		trolotov.setVerbose(true);
		
		//yhdistä
		try {
			trolotov.connect("irc.nebula.fi");
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
