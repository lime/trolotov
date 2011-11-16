import org.jibble.pircbot.*;

/**
 * Itse botti. TODO Mitä se osaa tehdä?
 * 
 * @author lime
 * 
 */
public class IRCbotti extends PircBot {
	public Vastaaja[] vastaajat;

	public IRCbotti(String nimi) {
		this.setName(nimi);
	}

	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.equalsIgnoreCase("!aika")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": Kello on  " + time);
		}
	}
	
}
