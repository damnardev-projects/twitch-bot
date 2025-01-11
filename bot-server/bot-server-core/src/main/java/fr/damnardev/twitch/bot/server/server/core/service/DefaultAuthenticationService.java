package fr.damnardev.twitch.bot.server.server.core.service;

import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.server.port.primary.AuthenticationService;
import fr.damnardev.twitch.bot.server.port.secondary.AuthenticationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.ChatRepository;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

	private final AuthenticationRepository authenticationRepository;

	private final ChatRepository chatRepository;

	private final EventPublisher eventPublisher;

	@Override
	public void tryRenew() {
		if (isAuthenticated()) {
			return;
		}
		var updated = this.authenticationRepository.renew();
		if (updated) {
			this.chatRepository.reconnect();
			isAuthenticated();
		}
	}

	@Override
	public boolean isAuthenticated() {
		var isValid = this.authenticationRepository.isValid();
		var event = AuthenticatedStatusEvent.builder().value(isValid).build();
		this.eventPublisher.publish(event);
		return isValid;
	}

	@Override
	public boolean isInitialized() {
		return this.authenticationRepository.isInitialized();
	}

}
