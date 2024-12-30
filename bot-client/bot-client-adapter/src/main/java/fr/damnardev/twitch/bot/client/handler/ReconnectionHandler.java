package fr.damnardev.twitch.bot.client.handler;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Service
@Slf4j
public class ReconnectionHandler extends StompSessionHandlerAdapter {

	private final WebSocketStompClient stompClient;

	private final String username;

	private final String password;

	private final String url;

	private final ThreadPoolTaskExecutor taskExecutor;

	private final StatusService statusService;

	private StompSession session;

	public ReconnectionHandler(WebSocketStompClient stompClient, @Value("${spring.security.user.name}") String username, @Value("${spring.security.user.password}") String password, @Value("${spring.server.url}") String url, ThreadPoolTaskExecutor taskExecutor, StatusService statusService) {
		this.stompClient = stompClient;
		this.username = username;
		this.password = password;
		this.url = url;
		this.taskExecutor = taskExecutor;
		this.statusService = statusService;
	}

	public void connect() {
		this.statusService.connected(false);
		this.taskExecutor.execute(() -> {
			var headers = new WebSocketHttpHeaders();
			var authString = "%s:%s".formatted(this.username, this.password);
			var basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
			headers.add("Authorization", basicAuthValue);
			try {
				this.stompClient.connectAsync(this.url, headers, this).get();
				this.statusService.connected(true);
			}
			catch (Exception ex) {
				log.error("An error occurred while connecting to the WebSocket server: {}", ex.getMessage());
			}
		});
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		log.info("New WebSocket connection established with session id: {}", session.getSessionId());
		this.session = session;
		this.session.subscribe("/response/channels/fetchedAll", this);
		this.session.send("/request/channels/fetchAll", null);
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
		if (session != null && session.isConnected()) {
			this.statusService.connected(false);
			session.disconnect();
		}
		connect();
		try {
			TimeUnit.SECONDS.sleep(10);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
