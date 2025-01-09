package fr.damnardev.twitch.bot.client;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

@Component
public class StompSessionStorage {

	private StompSession session;

	public StompSession getSession() {
		return session;
	}

	public void setSession(StompSession session) {
		this.session = session;
	}

}
