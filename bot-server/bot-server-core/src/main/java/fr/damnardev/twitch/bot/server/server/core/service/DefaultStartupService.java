package fr.damnardev.twitch.bot.server.server.core.service;

import fr.damnardev.twitch.bot.server.DomainService;
import fr.damnardev.twitch.bot.server.model.event.ApplicationStartedEvent;
import fr.damnardev.twitch.bot.server.port.primary.StartupService;
import fr.damnardev.twitch.bot.server.port.secondary.AuthenticationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.ChatRepository;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.StreamRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.UpdateChannelRepository;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DefaultStartupService implements StartupService {

	private final AuthenticationRepository authenticationRepository;

	private final FindChannelRepository findChannelRepository;

	private final ChatRepository chatRepository;

	private final StreamRepository streamRepository;

	private final EventPublisher eventPublisher;

	private final UpdateChannelRepository updateChannelRepository;

	@Override
	public void run() {
		var generated = this.authenticationRepository.renew();
		if (!generated) {
			System.exit(-1);
		}
		var channels = this.findChannelRepository.findAllEnabled();
		this.chatRepository.joinAll(channels);
		this.chatRepository.reconnect();
		channels = this.streamRepository.computeOnline(channels);
		this.updateChannelRepository.updateAll(channels);
		this.eventPublisher.publish(ApplicationStartedEvent.builder().build());
	}

}