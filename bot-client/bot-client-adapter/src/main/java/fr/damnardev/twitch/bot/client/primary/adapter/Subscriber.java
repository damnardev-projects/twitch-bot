package fr.damnardev.twitch.bot.client.primary.adapter;

import org.springframework.messaging.simp.stomp.StompHeaders;

public interface Subscriber<T> {

	String getDestination();

	Class<T> getPayloadType();

	void handleEvent(StompHeaders headers, T payload);

}
