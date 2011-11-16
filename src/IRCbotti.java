import org.jibble.pircbot.*;

/**
 * Itse botti. TODO Mitä se osaa tehdä?
 * 
 * @author lime
 * 
 */
public class IRCbotti extends PircBot {
	
	Delegoija delegoija;

	public IRCbotti(String nimi) {
		this.setName(nimi);
		delegoija = new Delegoija();
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// TODO Auto-generated method stub
		String vastaus = this.delegoija.kasitteleViesti(message, sender);
		if(vastaus != null) {
			this.sendMessage(channel, vastaus);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibble.pircbot.PircBot#onPrivateMessage(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		// TODO Auto-generated method stub
		String vastaus = this.delegoija.kasitteleYksityisViesti(message, sender);
		if(vastaus != null) {
			this.sendMessage(sender, vastaus);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibble.pircbot.PircBot#onMode(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onMode(String channel, String sourceNick,
			String sourceLogin, String sourceHostname, String mode) {
		// TODO Auto-generated method stub
		super.onMode(channel, sourceNick, sourceLogin, sourceHostname, mode);
	}

}
