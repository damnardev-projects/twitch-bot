package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.DeleteChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
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
class DeleteChannelProcessorTests {

	@InjectMocks
	private DeleteChannelProcessor deleteChannelProcessor;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private DeleteChannelRepository deleteChannelRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenChannelNotFound() {
		// Given
		var form = ActionForm.DELETE_CHANNEL.builder().resourceId(1L).build();

		given(this.findChannelRepository.findById(1L)).willReturn(Optional.empty());

		// When
		var result = assertThatThrownBy(() -> this.deleteChannelProcessor.process(form));

		// Then
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.deleteChannelRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Channel not found");
	}

	@Test
	void process_shouldDeleteChannel_whenChannelFound() {
		// Given
		var form = ActionForm.DELETE_CHANNEL.builder().resourceId(1L).build();

		var channel = Channel.builder().id(1L).build();
		given(this.findChannelRepository.findById(1L)).willReturn(Optional.of(channel));

		// When
		this.deleteChannelProcessor.process(form);

		// Then
		var expected = channel.toBuilder().build();
		then(this.findChannelRepository).should().findById(1L);
		then(this.deleteChannelRepository).should().delete(expected);
		then(this.eventPublisher).should().publish(ChannelDeletedEvent.builder().value(expected).build());
		then(this.findChannelRepository).should().findById(1L);
		verifyNoMoreInteractions(this.findChannelRepository, this.deleteChannelRepository, this.eventPublisher);
	}

}
