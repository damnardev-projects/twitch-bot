package fr.damnardev.twitch.bot.server.server.core.service.action.channel;


import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.SaveChannelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CreateChannelProcessorTests {

	@InjectMocks
	private CreateChannelProcessor createChannelProcessor;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private SaveChannelRepository saveChannelRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenNameIsNull() {
		// Given
		var form = ActionForm.CREATE_CHANNEL.builder().build();

		// When
		var result = assertThatThrownBy(() -> this.createChannelProcessor.process(form));

		// Then
		verifyNoMoreInteractions(this.findChannelRepository, this.saveChannelRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Channel name is required");
	}

	@Test
	void process_shouldThrowException_whenChannelNameNotFound() {
		// Given
		var channelName = "channelName";
		var form = ActionForm.CREATE_CHANNEL.builder().value(channelName).build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(Channel.builder().name(channelName).build()));

		// When
		var result = assertThatThrownBy(() -> this.createChannelProcessor.process(form));

		// Then
		then(this.findChannelRepository).should().findByName(channelName);
		verifyNoMoreInteractions(this.findChannelRepository, this.saveChannelRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Channel already exists");
	}

	@Test
	void process_shouldCreateChannel_whenChannelNotExist() {
		// Given
		var channelName = "channelName";
		var form = ActionForm.CREATE_CHANNEL.builder().value(channelName).build();
		var channel = Channel.builder().build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.empty());
		given(this.saveChannelRepository.save(channelName)).willReturn(channel);

		// When
		this.createChannelProcessor.process(form);

		// Then
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.saveChannelRepository).should().save(channelName);
		then(this.eventPublisher).should().publish(ChannelCreatedEvent.builder().value(channel).build());
		verifyNoMoreInteractions(this.findChannelRepository, this.saveChannelRepository, this.eventPublisher);
	}

}
