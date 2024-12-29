package fr.damnardev.twitch.bot.client.configuration;

import java.util.Base64;
import java.util.concurrent.ExecutionException;

import fr.damnardev.twitch.bot.client.handler.DefaultWebSocketHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Configuration
public class StompConfiguration {

	@Bean
	public WebSocketStompClient webSocketStompClient() {
		var stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		return stompClient;
	}

	@Bean
	public DefaultWebSocketHandler webSocketHandler() {
		return new DefaultWebSocketHandler();
	}

	@Bean
	public StompSession stompSession(WebSocketStompClient stompClient, DefaultWebSocketHandler sessionHandler, @org.springframework.beans.factory.annotation.Value("${spring.security.user.name}") String username, @Value("${spring.security.user.password}") String password, @Value("${spring.server.url}") String url) throws ExecutionException, InterruptedException {
		var headers = new WebSocketHttpHeaders();
		var basicAuthValue = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		headers.add("Authorization", basicAuthValue);
		return stompClient.connectAsync(url, headers, sessionHandler).get();
	}

}
