package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.primary.action.ActionService;
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
class ActionControllerTests {

	@InjectMocks
	private ActionController actionController;

	@Mock
	private ActionService actionService;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Test
	void fetchAll_shouldInvokeFetchAllChannelServiceFetchAll_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);
		var form = ActionForm.FETCH_AUTHENTICATED.builder().build();

		// When
		this.actionController.action(form);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.actionService).should().process(form);
		verifyNoMoreInteractions(this.executor);
	}

}
