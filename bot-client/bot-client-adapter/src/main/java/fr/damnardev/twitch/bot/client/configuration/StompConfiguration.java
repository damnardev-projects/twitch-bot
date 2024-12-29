package fr.damnardev.twitch.bot.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
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

}
