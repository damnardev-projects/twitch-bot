package fr.damnardev.twitch.bot.client.port.primary;

import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;

public interface ApplicationService {

	void handleChannelFetchedAllEvent(ChannelFetchedAllEvent event);

	void handleChannelCreatedEvent(ChannelCreatedEvent event);

	void handleChannelUpdatedEvent(ChannelUpdatedEvent event);

	void handleChannelDeletedEvent(ChannelDeletedEvent event);

	void handleRaidConfigurationFetchedEvent(RaidConfigurationFetchedEvent payload);

	void handleRaidConfigurationUpdatedEvent(RaidConfigurationUpdatedEvent payload);

	void connected();

}
