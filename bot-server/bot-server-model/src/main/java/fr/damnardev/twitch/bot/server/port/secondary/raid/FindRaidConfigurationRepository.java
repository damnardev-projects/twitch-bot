package fr.damnardev.twitch.bot.server.port.secondary.raid;

import java.util.List;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.RaidConfiguration;

public interface FindRaidConfigurationRepository {

	Optional<RaidConfiguration> findByChannelId(long channelId);

	List<RaidConfiguration> findAll();

}
