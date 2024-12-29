package fr.damnardev.twitch.bot.server.port.secondary.raid;

import fr.damnardev.twitch.bot.server.model.RaidConfiguration;

public interface UpdateRaidConfigurationRepository {

	void update(RaidConfiguration raidConfiguration);

}
