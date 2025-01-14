package fr.damnardev.twitch.bot.client.primary.adapter;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.damnardev.twitch.bot.client.StompSessionWriter;
import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.client.property.BotProperties;
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

	private final StompSessionWriter stompSessionWriter;

	private final StatusService statusService;

	private final ApplicationService applicationService;

	private final Map<String, Subscriber<?>> subscriberByDestination;

	private final Map<Type, Subscriber<?>> subscriberByType;

	public BotStompHandler(WebSocketStompClient stompClient, BotProperties botProperties, ThreadPoolTaskExecutor taskExecutor, StompSessionWriter stompSessionWriter, ClientRepository clientRepository, ApplicationService applicationService, StatusService statusService, Set<Subscriber<?>> subscribers) {
		this.stompClient = stompClient;
		this.botProperties = botProperties;
		this.taskExecutor = taskExecutor;
		this.stompSessionWriter = stompSessionWriter;
		this.statusService = statusService;
		this.applicationService = applicationService;
		this.subscriberByDestination = subscribers.stream().collect(Collectors.toMap(Subscriber::getDestination, Function.identity()));
		this.subscriberByType = subscribers.stream().collect(Collectors.toMap(Subscriber::getPayloadType, Function.identity()));
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
		this.subscriberByType.values().forEach((subscriber) -> session.subscribe(subscriber.getDestination(), this));
		this.stompSessionWriter.setSession(session);
		this.statusService.handleConnectionStatus(true);
		this.applicationService.connected();
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		var destination = headers.getDestination();
		var subscriber = this.subscriberByDestination.get(destination);
		if (subscriber != null) {
			return subscriber.getPayloadType();
		}
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		log.info("Received message: {}", payload);
		@SuppressWarnings("unchecked")
		var subscriber = (Subscriber<Object>) this.subscriberByType.get(payload.getClass());
		if (subscriber != null) {
			subscriber.handleEvent(headers, payload);
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
			this.stompSessionWriter.setSession(null);
		}
	}

}
