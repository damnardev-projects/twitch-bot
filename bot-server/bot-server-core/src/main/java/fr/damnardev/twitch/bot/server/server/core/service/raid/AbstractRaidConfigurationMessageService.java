package fr.damnardev.twitch.bot.server.server.core.service.raid;

import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.server.model.RaidConfiguration;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractRaidConfigurationMessageService {

	protected final FindChannelRepository findChannelRepository;

	protected final FindRaidConfigurationRepository findRaidConfigurationRepository;

	protected final EventPublisher eventPublisher;

	protected RaidConfiguration getRaidConfiguration(String name) {
		var optionalChannel = this.findChannelRepository.findByName(name);
		if (optionalChannel.isEmpty()) {
			throw new BusinessException("Channel not found");
		}
		var optionalRaidConfiguration = this.findRaidConfigurationRepository.findByChannel(optionalChannel.get());
		if (optionalRaidConfiguration.isEmpty()) {
			throw new BusinessException("Channel Raid Configuration not found");
		}
		return optionalRaidConfiguration.get();
	}

}
