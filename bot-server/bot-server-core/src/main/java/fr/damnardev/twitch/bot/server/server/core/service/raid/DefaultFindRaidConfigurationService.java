package fr.damnardev.twitch.bot.server.server.core.service.raid;

import fr.damnardev.twitch.bot.server.DomainService;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.server.port.primary.raid.FetchRaidConfigurationService;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.server.core.service.DefaultTryService;

@DomainService
public class DefaultFindRaidConfigurationService extends AbstractRaidConfigurationMessageService implements FetchRaidConfigurationService {

	private final DefaultTryService tryService;

	public DefaultFindRaidConfigurationService(FindChannelRepository findChannelRepository, FindRaidConfigurationRepository findRaidConfigurationRepository, EventPublisher eventPublisher, DefaultTryService tryService) {
		super(findChannelRepository, findRaidConfigurationRepository, eventPublisher);
		this.tryService = tryService;
	}

	@Override
	public void process(String name) {
		this.tryService.doTry(this::doInternal, name);
	}

	private void doInternal(String name) {
		var raidConfiguration = getRaidConfiguration(name);
		if (raidConfiguration == null) {
			return;
		}
		var event = RaidConfigurationFetchedEvent.builder().raidConfiguration(raidConfiguration).build();
		this.eventPublisher.publish(event);
	}

}
