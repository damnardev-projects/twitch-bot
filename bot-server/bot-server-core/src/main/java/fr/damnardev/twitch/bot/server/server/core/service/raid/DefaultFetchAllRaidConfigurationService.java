package fr.damnardev.twitch.bot.server.server.core.service.raid;

import fr.damnardev.twitch.bot.server.DomainService;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.server.port.primary.raid.FetchAllRaidConfigurationService;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.server.core.service.DefaultTryService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DefaultFetchAllRaidConfigurationService implements FetchAllRaidConfigurationService {

	private final DefaultTryService tryService;

	private final FindRaidConfigurationRepository findRaidConfigurationRepository;

	private final EventPublisher eventPublisher;

	@Override
	public void fetchAll() {
		this.tryService.doTry(this::raidConfigurations);
	}

	private void raidConfigurations() {
		var configurations = this.findRaidConfigurationRepository.findAll();
		var event = RaidConfigurationFetchedAllEvent.builder().value(configurations).build();
		this.eventPublisher.publish(event);
	}

}
