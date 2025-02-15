package fr.damnardev.twitch.bot.server.primary.adapter.twitch;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.RaidEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.eventsub.events.ChannelRaidEvent;
import fr.damnardev.twitch.bot.server.model.form.ChannelRaidEventForm;
import fr.damnardev.twitch.bot.server.port.primary.ChannelRaidEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class RaidEventConsumerTests {

	@InjectMocks
	private RaidEventConsumer consumer;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Mock
	private TwitchClient twitchClient;

	@Mock
	private ChannelRaidEventService handler;

	@Test
	void init_shouldRegisterHandler_whenCalled() {
		// Given
		var eventManager = mock(EventManager.class);
		var eventHandler = mock(SimpleEventHandler.class);

		given(this.twitchClient.getEventManager()).willReturn(eventManager);
		given(eventManager.getEventHandler(SimpleEventHandler.class)).willReturn(eventHandler);
		given(eventHandler.onEvent(eq(ChannelRaidEvent.class), any())).willReturn(null);

		// When
		this.consumer.init();

		// Then
		then(this.twitchClient).should().getEventManager();
		then(eventManager).should().getEventHandler(SimpleEventHandler.class);
		then(eventHandler).should().onEvent(eq(RaidEvent.class), any());
		verifyNoMoreInteractions(this.executor, this.twitchClient, this.handler, eventManager, eventHandler);
	}

	@Test
	void process_shouldExecuteHandler_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);
		var event = mock(RaidEvent.class);
		var channel = mock(EventChannel.class);
		var model = ChannelRaidEventForm.builder().channelId(2L).channelName("channel").raiderId(1L).raiderName("raider").build();

		given(event.getChannel()).willReturn(new EventChannel("2", "channel"));
		given(event.getRaider()).willReturn(new EventUser("1", "raider"));

		// When
		this.consumer.handleEvent(event);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(event).should(times(2)).getChannel();
		then(event).should(times(2)).getRaider();
		then(this.handler).should().raid(model);
		verifyNoMoreInteractions(this.executor, this.twitchClient, this.handler, event, channel);
	}

}
