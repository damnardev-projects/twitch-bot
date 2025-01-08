package fr.damnardev.twitch.bot.server.secondary.adapter;

import java.util.Map;

import fr.damnardev.twitch.bot.server.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.server.model.event.Event;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationUpdatedEvent;
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
		internalPublish("/response/channels/fetchedAll", "channelFetchedAll", event);
	}

	@Override
	public void publish(ChannelCreatedEvent event) {
		internalPublish("/response/channels/created", "channelCreated", event);
	}

	@Override
	public void publish(ChannelUpdatedEvent event) {
		internalPublish("/response/channels/updated", "channelUpdated", event);
	}

	@Override
	public void publish(ChannelDeletedEvent event) {
		internalPublish("/response/channels/deleted", "channelDeleted", event);
	}

	@Override
	public void publish(RaidConfigurationFetchedAllEvent event) {
		internalPublish("/response/raids/fetchedAll", "raidFetchedAll", event);
	}

	@Override
	public void publish(RaidConfigurationUpdatedEvent event) {
		internalPublish("/response/raids/updated", "raidUpdated", event);
	}

	private <T> void internalPublish(String destination, String type, T event) {
		Map<String, Object> headers = Map.of("type", type);
		this.messagingTemplate.convertAndSend(destination, event, headers);
	}

}
