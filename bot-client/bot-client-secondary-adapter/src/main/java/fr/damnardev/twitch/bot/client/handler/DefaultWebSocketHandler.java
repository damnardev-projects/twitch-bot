package fr.damnardev.twitch.bot.client.handler;

import java.lang.reflect.Type;

import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Slf4j
public class DefaultWebSocketHandler extends StompSessionHandlerAdapter {

	private StompSession session;

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		log.info("New WebSocket connection established with session id: {}", session.getSessionId());
		this.session = session;
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		log.info("Received message: {}", payload);
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
		log.info("An exception occurred: {}", exception.getMessage());
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		log.info("An error occurred: {}", exception.getMessage());
	}

	public void send(String value) {
		this.session.send("/app/request", value);
	}

}
