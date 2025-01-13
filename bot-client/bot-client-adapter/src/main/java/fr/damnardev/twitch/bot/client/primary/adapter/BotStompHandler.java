package fr.damnardev.twitch.bot.client.primary.adapter;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import fr.damnardev.twitch.bot.client.StompSessionStorage;
import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.client.property.BotProperties;
import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import lombok.extern.slf4j.Slf4j;

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

	private final BotProperties botProperties;

	private final ThreadPoolTaskExecutor taskExecutor;

	private final StompSessionStorage stompSessionStorage;

	private final ApplicationService applicationService;

	private final StatusService statusService;

	private final ChannelRepository channelRepository;

	public BotStompHandler(WebSocketStompClient stompClient, BotProperties botProperties, ThreadPoolTaskExecutor taskExecutor, StompSessionStorage stompSessionStorage, ClientRepository clientRepository, ApplicationService applicationService, StatusService statusService, ChannelRepository channelRepository) {
		this.stompClient = stompClient;
		this.botProperties = botProperties;
		this.taskExecutor = taskExecutor;
		this.stompSessionStorage = stompSessionStorage;
		this.applicationService = applicationService;
		this.statusService = statusService;
		this.channelRepository = channelRepository;
		clientRepository.setCallback(this::connect);
	}

	public void connect() {
		this.taskExecutor.execute(() -> {
			var headers = new WebSocketHttpHeaders();
			var authString = "%s:%s".formatted(this.botProperties.getUsername(), this.botProperties.getPassword());
			var basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
			headers.add("Authorization", basicAuthValue);
			try {
				this.stompClient.connectAsync(this.botProperties.getUrl(), headers, this).get();
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				log.error("An error occurred while connecting to the WebSocket server: {}", ex.getMessage());
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
		session.subscribe("/response/channels/created", this);
		session.subscribe("/response/channels/updated", this);
		session.subscribe("/response/channels/deleted", this);
		session.subscribe("/response/authenticated/status", this);
		this.stompSessionStorage.setSession(session);
		this.statusService.handleConnectionStatus(true);
		this.channelRepository.fetchAll();
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		var destination = headers.getDestination();
		if ("/response/channels/fetchedAll".equals(destination)) {
			return ChannelFetchedAllEvent.class;
		}
		if ("/response/channels/created".equals(destination)) {
			return ChannelCreatedEvent.class;
		}
		if ("/response/channels/updated".equals(destination)) {
			return ChannelUpdatedEvent.class;
		}
		if ("/response/channels/deleted".equals(destination)) {
			return ChannelDeletedEvent.class;
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
			this.applicationService.handleChannelFetchedAllEvent(event);
		}
		if (payload instanceof ChannelCreatedEvent event) {
			this.applicationService.handlerChannelCreatedEvent(event);
		}
		if (payload instanceof ChannelUpdatedEvent event) {
			this.applicationService.handleChannelUpdatedEvent(event);
		}
		if (payload instanceof ChannelDeletedEvent event) {
			this.applicationService.handleChannelDeletedEvent(event);
		}
		if (payload instanceof AuthenticatedStatusEvent event) {
			this.statusService.handleAuthenticationStatus(event.value());
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
			this.statusService.handleConnectionStatus(false);
			this.stompSessionStorage.setSession(null);
		}
	}

}
