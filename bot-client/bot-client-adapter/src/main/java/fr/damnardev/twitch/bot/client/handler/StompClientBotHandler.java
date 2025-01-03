package fr.damnardev.twitch.bot.client.handler;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import fr.damnardev.twitch.bot.client.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.client.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.client.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.client.port.primary.ChannelService;
import fr.damnardev.twitch.bot.client.port.primary.RaidConfigurationService;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.channel.CreateChannelRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
public class StompClientBotHandler extends StompSessionHandlerAdapter implements
		CreateChannelRepository {

	private final WebSocketStompClient stompClient;

	private final String username;

	private final String password;

	private final String url;

	private final ThreadPoolTaskExecutor taskExecutor;

	private final StatusService statusService;

	private final ChannelService channelService;

	private final RaidConfigurationService raidConfigurationService;

	private StompSession session;

	public StompClientBotHandler(WebSocketStompClient stompClient, @Value("${spring.security.user.name}") String username,
			@Value("${spring.security.user.password}") String password,
			@Value("${spring.server.url}") String url,
			ThreadPoolTaskExecutor taskExecutor,
			StatusService statusService,
			@Lazy ChannelService channelService, RaidConfigurationService raidConfigurationService) {
		this.stompClient = stompClient;
		this.username = username;
		this.password = password;
		this.url = url;
		this.taskExecutor = taskExecutor;
		this.statusService = statusService;
		this.channelService = channelService;
		this.raidConfigurationService = raidConfigurationService;
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
		this.session.subscribe("/response/channels/created", this);
		this.session.subscribe("/response/raids/fetchedAll", this);
		this.session.send("/request/channels/fetchAll", null);
		this.session.send("/request/raids/fetchAll", null);
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
		if ("/response/raids/fetchedAll".equals(destination)) {
			return RaidConfigurationFetchedAllEvent.class;
		}
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		if (payload instanceof ChannelFetchedAllEvent) {
			this.channelService.fetchAll(((ChannelFetchedAllEvent) payload));
		}
		if (payload instanceof ChannelCreatedEvent) {
			this.channelService.create(((ChannelCreatedEvent) payload));
		}
		if (payload instanceof RaidConfigurationFetchedAllEvent) {
			this.raidConfigurationService.fetchAll(((RaidConfigurationFetchedAllEvent) payload));
		}
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
		try {
			TimeUnit.SECONDS.sleep(10);
			connect();
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void create(CreateChannelForm event) {
		this.session.send("/request/channels/create", event);
	}

}
