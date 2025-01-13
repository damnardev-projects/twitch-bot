package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.StartupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ApplicationStartupListenerTests {

	@InjectMocks
	private ApplicationStartupListener applicationStartupListener;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Mock
	private StartupService startupService;

	@Test
	void run_shouldInvokeStartupServiceRun_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);

		// When
		this.applicationStartupListener.run(null);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.startupService).should().run(any(), any());
		verifyNoMoreInteractions(this.executor, this.startupService);
	}

}
