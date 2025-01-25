package fr.damnardev.twitch.bot.server.server.core.service.action.raid;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
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
class FetchRaidConfigurationProcessorTests {

	@InjectMocks
	private FetchRaidConfigurationProcessor fetchRaidConfigurationProcessor;

	@Mock
	private FindRaidConfigurationRepository findRaidConfigurationRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenRaidConfigurationNotFound() {
		// Given
		var form = ActionForm.FETCH_RAID.builder().resourceId(1L).build();

		given(this.findRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.empty());

		// When
		var result = assertThatThrownBy(() -> this.fetchRaidConfigurationProcessor.process(form));

		// Then
		then(this.findRaidConfigurationRepository).should().findByChannelId(1L);
		verifyNoMoreInteractions(this.findRaidConfigurationRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Raid Configuration not found");
	}

	@Test
	void process_shouldPublishEvent_whenRaidConfigurationFound() {
		// Given
		var form = ActionForm.FETCH_RAID.builder().resourceId(1L).build();
		var raidConfiguration = RaidConfiguration.builder().build();

		given(this.findRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.of(raidConfiguration));

		// When
		this.fetchRaidConfigurationProcessor.process(form);

		// Then
		then(this.findRaidConfigurationRepository).should().findByChannelId(1L);
		then(this.eventPublisher).should().publish(RaidConfigurationFetchedEvent.builder().value(raidConfiguration).build());
		verifyNoMoreInteractions(this.findRaidConfigurationRepository, this.eventPublisher);

	}

}
