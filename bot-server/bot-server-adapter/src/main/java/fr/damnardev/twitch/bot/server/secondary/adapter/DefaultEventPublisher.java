package fr.damnardev.twitch.bot.server.secondary.adapter;

import java.util.Map;

import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;
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
	public void publish(Exception ex) {
		log.error("Publishing exception", ex);
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
	public void publish(RaidConfigurationFetchedEvent event) {
		internalPublish("/response/raids/fetched", "raidFetched", event);
	}

	@Override
	public void publish(RaidConfigurationUpdatedEvent event) {
		internalPublish("/response/raids/updated", "raidUpdated", event);
	}

	@Override
	public void publish(AuthenticatedStatusEvent event) {
		internalPublish("/response/authenticated/status", "authenticatedStatus", event);
	}

	private <T> void internalPublish(String destination, String type, T event) {
		log.info("Publishing event {} to destination {}", event, destination);
		Map<String, Object> headers = Map.of("type", type);
		this.messagingTemplate.convertAndSend(destination, event, headers);
	}

}
