package fr.damnardev.twitch.bot.client.port.primary;

import fr.damnardev.twitch.bot.client.model.event.RaidConfigurationFetchedAllEvent;

public interface RaidConfigurationService {

	void fetchAll(RaidConfigurationFetchedAllEvent event);

}
