import java.io.UnsupportedEncodingException;

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
		this.setAutoNickChange(true);
		this.setLogin(nimi.toLowerCase());
		try {
			this.setEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		delegoija = new Delegoija();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibble.pircbot.PircBot#onMessage(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// TODO Thread?
		String vastaus = this.delegoija.kasitteleViesti(message, sender);
		if (vastaus != null) {
			this.sendMessage(channel, sender + ": " + vastaus);
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
		String vastaus = this.delegoija
				.kasitteleYksityisViesti(message, sender);
		if (vastaus != null) {
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

	@Override
	protected void onUserList(String channel, User[] users) {
		this.delegoija.kasitteleBotinLiittyminen(channel, users);
	}

	@Override
	protected void onJoin(String channel, String sender, String login,
			String hostname) {
		this.delegoija.kasitteleIrkkaajanLiittyminen(channel, sender, login,
				hostname);
	}

	@Override
	protected void onPart(String channel, String sender, String login,
			String hostname) {
		this.delegoija.kasitteleIrkkaajanPoistuminen(channel, sender, login,
				hostname);
	}

	@Override
	protected void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		this.delegoija.kasitteleIrkkaajanNickinMuuttuminen(oldNick, login,
				hostname, newNick);
	}
}