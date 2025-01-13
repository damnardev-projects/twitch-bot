package fr.damnardev.twitch.bot.client;

import lombok.Data;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

@Component
@Data
public class StompSessionStorage {

	private StompSession session;

}
