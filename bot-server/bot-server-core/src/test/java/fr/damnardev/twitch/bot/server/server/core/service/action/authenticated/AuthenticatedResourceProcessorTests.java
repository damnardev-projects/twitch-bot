package fr.damnardev.twitch.bot.server.server.core.service.action.authenticated;

import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.AuthenticationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AuthenticatedResourceProcessorTests {

	@InjectMocks
	private AuthenticatedResourceProcessor authenticatedResourceProcessor;

	@Mock
	private AuthenticationRepository authenticationRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Test
	void getActionKey_shouldReturnExpectedValue() {
		// When
		var result = this.authenticatedResourceProcessor.getActionKey();

		// Then
		assertThat(result).isEqualTo(ActionKey.FETCH_AUTHENTICATED);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void process_shouldPublishEvent_whenCalled(boolean authenticated) {
		// Given

		given(this.authenticationRepository.isValid()).willReturn(authenticated);

		// When
		this.authenticatedResourceProcessor.process(null);

		// Then
		then(this.authenticationRepository).should().isValid();
		then(this.eventPublisher).should().publish(AuthenticatedStatusEvent.builder().value(authenticated).build());
		verifyNoMoreInteractions(this.authenticationRepository, this.eventPublisher);

	}

}
