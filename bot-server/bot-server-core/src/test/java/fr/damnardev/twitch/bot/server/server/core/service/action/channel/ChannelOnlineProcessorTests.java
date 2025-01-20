package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
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
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ChannelOnlineProcessorTests {

	@InjectMocks
	private ChannelOnlineProcessor channelOnlineProcessor;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private UpdateChannelRepository updateChannelRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenChannelNotFound() {
		// Given
		var form = ActionForm.UPDATE_RAID_ONLINE.builder().resourceId(1L).value(true).build();

		given(this.findChannelRepository.findById(1L)).willReturn(Optional.empty());

		// When
		var result = assertThatThrownBy(() -> this.channelOnlineProcessor.process(form));

		// Then
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.updateChannelRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Channel not found");
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void process_shouldUpdateChannel_whenChannelFound(boolean value) {
		// Given
		var form = ActionForm.UPDATE_RAID_ONLINE.builder().resourceId(1L).value(value).build();

		var channel = Channel.builder().id(1L).build();
		given(this.findChannelRepository.findById(1L)).willReturn(Optional.of(channel));

		// When
		this.channelOnlineProcessor.process(form);

		// Then
		then(this.findChannelRepository).should().findById(1L);
		then(this.updateChannelRepository).should().update(channel.toBuilder().online(value).build());
		then(this.eventPublisher).should().publish(ChannelUpdatedEvent.builder().value(channel.toBuilder().online(value).build()).build());
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.updateChannelRepository, this.eventPublisher);
	}

}
