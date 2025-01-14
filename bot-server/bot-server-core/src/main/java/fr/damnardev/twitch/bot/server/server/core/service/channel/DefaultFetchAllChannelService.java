package fr.damnardev.twitch.bot.server.server.core.service.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.DefaultTryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class DefaultFetchAllChannelService implements FetchAllChannelService {

	private final DefaultTryService tryService;

	private final FindChannelRepository findChannelRepository;

	private final EventPublisher eventPublisher;

	@Override
	public void fetchAll() {
		this.tryService.doTry(this::doInternal);
	}

	private void doInternal() {
		var channels = this.findChannelRepository.findAll();
		var event = ChannelFetchedAllEvent.builder().value(channels).build();
		this.eventPublisher.publish(event);
	}

}
