package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.StreamRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.UpdateChannelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ChannelEnabledProcessorTests {

	@InjectMocks
	private ChannelEnabledProcessor channelEnabledProcessor;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private StreamRepository streamRepository;

	@Mock
	private UpdateChannelRepository updateChannelRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenChannelNotFound() {
		// Given
		var form = ActionForm.UPDATE_CHANNEL_ENABLED.builder().resourceId(1L).value(true).build();

		given(this.findChannelRepository.findById(1L)).willReturn(Optional.empty());

		// When
		var result = assertThatThrownBy(() -> this.channelEnabledProcessor.process(form));

		// Then
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.streamRepository, this.updateChannelRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Channel not found");
	}

	@Test
	void process_shouldUpdateChannel_whenChannelFoundAndEnabledFalse() {
		// Given
		var form = ActionForm.UPDATE_CHANNEL_ENABLED.builder().resourceId(1L).value(false).build();

		var channel = Channel.builder().id(1L).enabled(true).build();
		given(this.findChannelRepository.findById(1L)).willReturn(Optional.of(channel));

		// When
		this.channelEnabledProcessor.process(form);

		// Then
		var expected = channel.toBuilder().enabled(false).online(false).build();
		then(this.findChannelRepository).should().findById(1L);
		then(this.updateChannelRepository).should().update(expected);
		then(this.eventPublisher).should().publish(ChannelUpdatedEvent.builder().value(expected).build());
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.streamRepository, this.updateChannelRepository, this.eventPublisher);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void process_shouldUpdateChannel_whenChannelFoundAndEnabledTrue(boolean online) {
		// Given
		var form = ActionForm.UPDATE_CHANNEL_ENABLED.builder().resourceId(1L).value(true).build();

		var channel = Channel.builder().id(1L).enabled(false).build();
		given(this.findChannelRepository.findById(1L)).willReturn(Optional.of(channel));
		given(this.streamRepository.computeOnline(channel)).willReturn(channel.toBuilder().online(online).build());

		// When
		this.channelEnabledProcessor.process(form);

		// Then
		var expected = channel.toBuilder().enabled(true).online(online).build();
		then(this.findChannelRepository).should().findById(1L);
		then(this.streamRepository).should().computeOnline(channel);
		then(this.updateChannelRepository).should().update(expected);
		then(this.eventPublisher).should().publish(ChannelUpdatedEvent.builder().value(expected).build());
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.streamRepository, this.updateChannelRepository, this.eventPublisher);
	}

}
