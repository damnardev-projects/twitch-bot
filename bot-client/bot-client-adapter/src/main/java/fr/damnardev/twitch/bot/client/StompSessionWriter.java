package fr.damnardev.twitch.bot.client;

import lombok.Setter;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

@Component
@Setter
public class StompSessionWriter {

	private StompSession session;

	public void send(String destination) {
		if (this.session != null) {
			this.session.send(destination, null);
		}
	}

	public synchronized void send(String destination, Object payload) {
		if (this.session != null) {
			this.session.send(destination, payload);
		}
	}

}
