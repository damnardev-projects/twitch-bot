package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.server.model.form.UpdateRaidConfigurationForm;
import fr.damnardev.twitch.bot.server.port.primary.raid.FetchAllRaidConfigurationService;
import fr.damnardev.twitch.bot.server.port.primary.raid.UpdateRaidConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class RaidConfigurationControllerTests {

	@InjectMocks
	private RaidConfigurationController raidConfigurationController;

	@Mock
	private FetchAllRaidConfigurationService fetchAllRaidConfigurationService;

	@Mock
	private UpdateRaidConfigurationService updateRaidConfigurationService;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Test
	void fetchAll_shouldInvokeFetchAllRaidConfigurationServiceFetchAll_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);

		// When
		this.raidConfigurationController.fetchAll();

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.fetchAllRaidConfigurationService).should().fetchAll();
		verifyNoMoreInteractions(this.executor, this.fetchAllRaidConfigurationService);
	}

	@Test
	void update_shouldInvokeUpdateRaidConfigurationServiceUpdate_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);
		var form = UpdateRaidConfigurationForm.builder().build();

		// When
		this.raidConfigurationController.update(form);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.updateRaidConfigurationService).should().update(form);
		verifyNoMoreInteractions(this.executor, this.updateRaidConfigurationService);
	}

}
