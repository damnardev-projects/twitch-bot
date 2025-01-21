package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import java.util.Collections;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class FetchAllChannelProcessorTests {

	@InjectMocks
	private FetchAllChannelProcessor fetchAllChannelProcessor;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenChannelNotFound() {
		// Given
		var form = ActionForm.FETCH_ALL_CHANNEL;
		var channel = Channel.builder().build();
		var channels = Collections.singletonList(channel);

		given(this.findChannelRepository.findAll()).willReturn(channels);

		// When
		this.fetchAllChannelProcessor.process(form);

		// Then
		then(this.findChannelRepository).should().findAll();
		then(this.eventPublisher).should().publish(ChannelFetchedAllEvent.builder().value(channels).build());
		verifyNoMoreInteractions(this.findChannelRepository, this.eventPublisher);
	}

}
