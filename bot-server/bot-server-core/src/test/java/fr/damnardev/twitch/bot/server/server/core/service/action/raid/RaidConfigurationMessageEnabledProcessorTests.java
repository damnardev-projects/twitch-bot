package fr.damnardev.twitch.bot.server.server.core.service.action.raid;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.raid.UpdateRaidConfigurationRepository;
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
class RaidConfigurationMessageEnabledProcessorTests {

	@InjectMocks
	private RaidConfigurationMessageEnabledProcessor raidConfigurationMessageEnabledProcessor;

	@Mock
	private FindRaidConfigurationRepository findRaidConfigurationRepository;

	@Mock
	private UpdateRaidConfigurationRepository updateRaidConfigurationRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void process_shouldThrowException_whenRaidConfigurationNotFound() {
		// Given
		var form = ActionForm.UPDATE_RAID_MESSAGE_ENABLED.builder().resourceId(1L).build();

		given(this.findRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.empty());

		// When
		var result = assertThatThrownBy(() -> this.raidConfigurationMessageEnabledProcessor.process(form));

		// Then
		then(this.findRaidConfigurationRepository).should().findByChannelId(1L);
		verifyNoMoreInteractions(this.findRaidConfigurationRepository, this.eventPublisher);

		result.isInstanceOf(BusinessException.class).hasMessage("Raid Configuration not found");
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void process_shouldUpdateRaidConfiguration_whenRaidConfigurationFound(boolean value) {
		// Given
		var form = ActionForm.UPDATE_RAID_MESSAGE_ENABLED.builder().resourceId(1L).value(value).build();
		var raidConfiguration = RaidConfiguration.builder().raidMessageEnabled(!value).build();

		given(this.findRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.of(raidConfiguration));

		// When
		this.raidConfigurationMessageEnabledProcessor.process(form);

		// Then
		var expected = RaidConfiguration.builder().raidMessageEnabled(value).build();
		then(this.findRaidConfigurationRepository).should().findByChannelId(1L);
		then(this.updateRaidConfigurationRepository).should().update(expected);
		then(this.eventPublisher).should().publish(RaidConfigurationUpdatedEvent.builder().value(expected).build());
		verifyNoMoreInteractions(this.findRaidConfigurationRepository, this.updateRaidConfigurationRepository, this.eventPublisher);
	}

}