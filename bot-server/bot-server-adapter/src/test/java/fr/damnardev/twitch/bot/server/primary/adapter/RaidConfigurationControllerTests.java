package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.port.primary.raid.FetchAllRaidConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class RaidConfigurationControllerTests {

	@InjectMocks
	private RaidConfigurationController raidConfigurationController;

	@Mock
	private FetchAllRaidConfigurationService fetchAllRaidConfigurationService;

	@Test
	void fetchAll_shouldInvokeFetchAllRaidConfigurationServiceFetchAll_whenCalled() {
		// Given

		// When
		this.raidConfigurationController.fetchAll();

		// Then
		then(this.fetchAllRaidConfigurationService).should().fetchAll();
		verifyNoMoreInteractions(this.fetchAllRaidConfigurationService);
	}

}
