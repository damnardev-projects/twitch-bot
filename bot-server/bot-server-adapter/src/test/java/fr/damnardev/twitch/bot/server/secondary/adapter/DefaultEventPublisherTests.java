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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultEventPublisherTests {

	@InjectMocks
	private DefaultEventPublisher eventPublisher;

	@Mock
	private SimpMessagingTemplate publisher;

	@Test
	void publish_shouldInvokePublishChannelFetchedAll_whenCalled() {
		// Given
		var event = ChannelFetchedAllEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "channelFetchedAll");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/channels/fetchedAll", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishChannelCreated_whenCalled() {
		// Given
		var event = ChannelCreatedEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "channelCreated");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/channels/created", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishChannelUpdatedEvent_whenCalled() {
		// Given
		var event = ChannelUpdatedEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "channelUpdated");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/channels/updated", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishChannelDeletedEvent_whenCalled() {
		// Given
		var event = ChannelDeletedEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "channelDeleted");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/channels/deleted", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishRaidFetchedAll_whenCalled() {
		// Given
		var event = RaidConfigurationFetchedAllEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "raidFetchedAll");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/raids/fetchedAll", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishRaidFetched_whenCalled() {
		// Given
		var event = RaidConfigurationFetchedEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "raidFetched");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/raids/fetched", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishRaidUpdated_whenCalled() {
		// Given
		var event = RaidConfigurationUpdatedEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "raidUpdated");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/raids/updated", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

	@Test
	void publish_shouldInvokePublishAuthenticatedStatusEvent_whenCalled() {
		// Given
		var event = AuthenticatedStatusEvent.builder().build();
		Map<String, Object> headers = Map.of("type", "authenticatedStatus");

		// When
		this.eventPublisher.publish(event);

		// Then
		then(this.publisher).should().convertAndSend("/response/authenticated/status", event, headers);
		verifyNoMoreInteractions(this.publisher);
	}

}
