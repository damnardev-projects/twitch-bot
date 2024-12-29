package fr.damnardev.twitch.bot.server.server.core.service;

import fr.damnardev.twitch.bot.server.DomainService;
import fr.damnardev.twitch.bot.server.port.primary.AuthenticationService;
import fr.damnardev.twitch.bot.server.port.secondary.AuthenticationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.ChatRepository;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

	private final AuthenticationRepository authenticationRepository;

	private final ChatRepository chatRepository;

	@Override
	public void tryRenew() {
		if (this.authenticationRepository.isValid()) {
			return;
		}
		var updated = this.authenticationRepository.renew();
		if (updated) {
			this.chatRepository.reconnect();
		}
	}

	@Override
	public boolean isInitialized() {
		return this.authenticationRepository.isInitialized();
	}

}
