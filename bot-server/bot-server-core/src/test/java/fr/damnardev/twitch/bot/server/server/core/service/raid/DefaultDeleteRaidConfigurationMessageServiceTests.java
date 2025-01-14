package fr.damnardev.twitch.bot.server.server.core.service.raid;

import java.util.ArrayList;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.RaidConfiguration;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.server.model.event.ErrorEvent;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationUpdatedEvent;
import fr.damnardev.twitch.bot.server.model.form.DeleteRaidConfigurationMessageForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.raid.UpdateRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.server.core.service.DefaultTryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultDeleteRaidConfigurationMessageServiceTests {

	private DefaultDeleteRaidConfigurationMessageService deleteRaidConfigurationMessageService;

	private DefaultTryService tryService;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private FindRaidConfigurationRepository findRaidConfigurationRepository;

	@Mock
	private UpdateRaidConfigurationRepository updateRaidConfigurationRepository;

	@Mock
	private EventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		this.tryService = new DefaultTryService(this.eventPublisher);
		this.tryService = spy(this.tryService);
		this.deleteRaidConfigurationMessageService = new DefaultDeleteRaidConfigurationMessageService(this.findChannelRepository, this.findRaidConfigurationRepository, this.eventPublisher, this.tryService, this.updateRaidConfigurationRepository);
	}

	@Test
	void delete_shouldPublishEvent_whenChannelNotFound() {
		// Given
		var channelName = "channelName";
		var form = DeleteRaidConfigurationMessageForm.builder().channelName(channelName).build();
		var captor = ArgumentCaptor.forClass(ErrorEvent.class);

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.empty());

		// When
		this.deleteRaidConfigurationMessageService.delete(form);

		// Then
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.eventPublisher).should().publish(captor.capture());
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.findRaidConfigurationRepository, this.updateRaidConfigurationRepository, this.eventPublisher);

		var event = captor.getValue();
		assertThat(event.getException()).isNotNull()
				.isInstanceOf(BusinessException.class)
				.hasMessage("Channel not found");
	}

	@Test
	void delete_shouldPublishEvent_whenRaidConfigurationNotFound() {
		// Given
		var channelName = "channelName";
		var form = DeleteRaidConfigurationMessageForm.builder().channelName(channelName).build();
		var channel = Channel.builder().name(channelName).build();
		var captor = ArgumentCaptor.forClass(ErrorEvent.class);

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findRaidConfigurationRepository.findByChannel(channel)).willReturn(Optional.empty());

		// When
		this.deleteRaidConfigurationMessageService.delete(form);

		// Then
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findRaidConfigurationRepository).should().findByChannel(channel);
		then(this.eventPublisher).should().publish(captor.capture());
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.findRaidConfigurationRepository, this.updateRaidConfigurationRepository, this.eventPublisher);

		var event = captor.getValue();
		assertThat(event.getException()).isNotNull()
				.isInstanceOf(BusinessException.class)
				.hasMessage("Channel Raid Configuration not found");
	}

	@Test
	void delete_shouldPublishEvent_whenRaidConfigurationFound() {
		// Given
		var channelName = "channelName";
		var message = "my message";
		var form = DeleteRaidConfigurationMessageForm.builder().channelName(channelName).message(message).build();
		var channel = Channel.builder().name(channelName).build();
		var messages = new ArrayList<String>();
		messages.add(message);
		messages.add("another message");
		var raidConfiguration = RaidConfiguration.builder().messages(messages).build();
		var event = RaidConfigurationUpdatedEvent.builder().value(raidConfiguration).build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findRaidConfigurationRepository.findByChannel(channel)).willReturn(Optional.of(raidConfiguration));

		// When
		this.deleteRaidConfigurationMessageService.delete(form);

		// Then
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findRaidConfigurationRepository).should().findByChannel(channel);
		then(this.updateRaidConfigurationRepository).should().update(raidConfiguration);
		then(this.eventPublisher).should().publish(event);
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.findRaidConfigurationRepository, this.updateRaidConfigurationRepository, this.eventPublisher);
	}

}
