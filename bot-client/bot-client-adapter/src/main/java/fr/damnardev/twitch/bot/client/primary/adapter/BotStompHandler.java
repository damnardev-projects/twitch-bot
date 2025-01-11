package fr.damnardev.twitch.bot.client.primary.adapter;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import fr.damnardev.twitch.bot.client.StompSessionStorage;
import fr.damnardev.twitch.bot.client.port.primary.ChannelService;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
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
public class BotStompHandler extends StompSessionHandlerAdapter {

	private final WebSocketStompClient stompClient;

	private final String username;

	private final String password;

	private final String url;

	private final ThreadPoolTaskExecutor taskExecutor;

	private final StompSessionStorage stompSessionStorage;

	private final StatusService statusService;

	private final ChannelRepository channelRepository;

	private final ChannelService channelService;

	public BotStompHandler(WebSocketStompClient stompClient, @Value("${spring.security.user.name}") String username, @Value("${spring.security.user.password}") String password, @Value("${spring.server.url}") String url, ThreadPoolTaskExecutor taskExecutor, StompSessionStorage stompSessionStorage, ClientRepository clientRepository, StatusService statusService, ChannelRepository channelRepository, ChannelService channelService) {
		this.stompClient = stompClient;
		this.username = username;
		this.password = password;
		this.url = url;
		this.taskExecutor = taskExecutor;
		this.stompSessionStorage = stompSessionStorage;
		this.statusService = statusService;
		this.channelRepository = channelRepository;
		this.channelService = channelService;
		clientRepository.setCallback(this::connect);
	}

	public void connect() {
		this.taskExecutor.execute(() -> {
			var headers = new WebSocketHttpHeaders();
			var authString = "%s:%s".formatted(this.username, this.password);
			var basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
			headers.add("Authorization", basicAuthValue);
			try {
				this.stompClient.connectAsync(this.url, headers, this).get();
			}
			catch (Exception ex) {
				log.error("An error occurred while connecting to the WebSocket server: {}", ex.getMessage());
			}
		});
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		log.info("New WebSocket connection established with session id: {}", session.getSessionId());
		session.subscribe("/response/channels/fetchedAll", this);
		session.subscribe("/response/authenticated/status", this);
		this.stompSessionStorage.setSession(session);
		this.statusService.connected(true);
		this.channelRepository.fetchAll();
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		var destination = headers.getDestination();
		if ("/response/channels/fetchedAll".equals(destination)) {
			return ChannelFetchedAllEvent.class;
		}
		if ("/response/authenticated/status".equals(destination)) {
			return AuthenticatedStatusEvent.class;
		}
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		log.info("Received message: {}", payload);
		if (payload instanceof ChannelFetchedAllEvent event) {
			this.channelService.fetchAll(event);
		}
		if (payload instanceof AuthenticatedStatusEvent(Boolean value)) {
			this.statusService.authenticated(value);
		}
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
		log.info("An exception occurred: {}", exception.getMessage());
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		log.info("An error occurred: {}", exception.getMessage());
		if (session.isConnected()) {
			session.disconnect();
		}
		try {
			TimeUnit.SECONDS.sleep(10);
			connect();
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		finally {
			this.statusService.connected(false);
			this.stompSessionStorage.setSession(null);
		}
	}

}
