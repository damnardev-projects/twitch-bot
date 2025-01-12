package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.server.port.primary.AuthenticationService;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ClientControllerTests {

	@InjectMocks
	private ClientController clientController;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Mock
	private AuthenticationService authenticationService;

	@Test
	void fetch_shouldExecuteAuthenticationService_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);

		// When
		this.clientController.fetch();

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.authenticationService).should().isAuthenticated();
		verifyNoMoreInteractions(this.executor, this.authenticationService);
	}

}
