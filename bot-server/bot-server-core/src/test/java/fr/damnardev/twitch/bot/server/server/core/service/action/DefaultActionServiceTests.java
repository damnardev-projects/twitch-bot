package fr.damnardev.twitch.bot.server.server.core.service.action;

import java.util.Set;

import fr.damnardev.twitch.bot.model.exception.FatalException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.server.core.service.DefaultTryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultActionServiceTests {

	@Mock
	private EventPublisher eventPublisher;

	private DefaultActionService createService(Set<Processor<?>> processors) {
		var tryService = new DefaultTryService(this.eventPublisher);
		return new DefaultActionService(processors, tryService);
	}

	@Test
	void process_shouldPublishException_whenNoProcessorFound() {
		// Given
		var service = createService(Set.of());
		var form = ActionForm.FETCH_AUTHENTICATED.builder().build();

		// When
		service.process(form);

		// Then
		then(this.eventPublisher).should().publish(ArgumentMatchers.<FatalException>argThat((args) -> args.getMessage().equals("No processor found for key " + ActionKey.FETCH_AUTHENTICATED)));
		verifyNoMoreInteractions(this.eventPublisher);
	}

}
