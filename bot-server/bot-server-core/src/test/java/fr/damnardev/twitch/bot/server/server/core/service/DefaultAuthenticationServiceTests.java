package fr.damnardev.twitch.bot.server.server.core.service;

import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.server.port.secondary.AuthenticationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.ChatRepository;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultAuthenticationServiceTests {

	@InjectMocks
	private DefaultAuthenticationService startupService;

	@Mock
	private AuthenticationRepository authenticationRepository;

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private EventPublisher eventPublisher;

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void isInitialized_shouldReturnExpectedValue_whenCalled(boolean value) {
		// Given
		given(this.authenticationRepository.isInitialized()).willReturn(value);

		// When
		var result = this.startupService.isInitialized();

		// Then
		then(this.authenticationRepository).should().isInitialized();
		verifyNoMoreInteractions(this.authenticationRepository, this.chatRepository, this.eventPublisher);

		assertThat(result).isEqualTo(value);
	}

	@Test
	void tryRenew_shouldDoNothing_whenTokenIsValid() {
		// Given
		given(this.authenticationRepository.isValid()).willReturn(true);

		// When
		this.startupService.tryRenew();

		// Then
		then(this.eventPublisher).should().publish(AuthenticatedStatusEvent.builder().value(true).build());
		then(this.authenticationRepository).should().isValid();
		verifyNoMoreInteractions(this.authenticationRepository, this.chatRepository, this.eventPublisher);
	}

	@Test
	void tryRenew_shouldRenewAndReconnect_whenTokenIsInvalid() {
		// Given
		given(this.authenticationRepository.isValid()).willReturn(false);
		given(this.authenticationRepository.renew()).willReturn(true);

		// When
		this.startupService.tryRenew();

		// Then
		then(this.authenticationRepository).should(times(2)).isValid();
		then(this.eventPublisher).should(times(2)).publish(AuthenticatedStatusEvent.builder().value(false).build());
		then(this.authenticationRepository).should().renew();
		then(this.chatRepository).should().reconnect();
		verifyNoMoreInteractions(this.authenticationRepository, this.chatRepository, this.eventPublisher);
	}

	@Test
	void tryRenew_shouldRenewAndNotReconnect_whenTokenIsInvalid() {
		// Given
		given(this.authenticationRepository.isValid()).willReturn(false);
		given(this.authenticationRepository.renew()).willReturn(false);

		// When
		this.startupService.tryRenew();

		// Then
		then(this.authenticationRepository).should().isValid();
		then(this.eventPublisher).should().publish(AuthenticatedStatusEvent.builder().value(false).build());
		then(this.authenticationRepository).should().renew();
		verifyNoMoreInteractions(this.authenticationRepository, this.chatRepository, this.eventPublisher);
	}

}
