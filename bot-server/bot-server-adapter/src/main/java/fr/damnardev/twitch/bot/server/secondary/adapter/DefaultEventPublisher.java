package fr.damnardev.twitch.bot.server.secondary.adapter;

import java.util.Map;

import fr.damnardev.twitch.bot.server.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.server.model.event.Event;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultEventPublisher implements EventPublisher {

	private final SimpMessagingTemplate messagingTemplate;

	@Override
	public <T extends Event> void publish(T event) {
		// TODO: Implement this method
	}

	@Override
	public void publish(ChannelFetchedAllEvent event) {
		Map<String, Object> headers = Map.of("type", "channelFetchedAll");
		this.messagingTemplate.convertAndSend("/response/channels/fetchedAll", event, headers);
	}

	@Override
	public void publish(RaidConfigurationFetchedAllEvent event) {
		Map<String, Object> headers = Map.of("type", "raidFetchedAll");
		this.messagingTemplate.convertAndSend("/response/raids/fetchedAll", event, headers);
	}

}
