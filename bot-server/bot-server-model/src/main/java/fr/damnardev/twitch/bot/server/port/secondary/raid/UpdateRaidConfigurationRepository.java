package fr.damnardev.twitch.bot.server.port.secondary.raid;

import fr.damnardev.twitch.bot.model.RaidConfiguration;

public interface UpdateRaidConfigurationRepository {

	void update(RaidConfiguration raidConfiguration);

}
