package fr.damnardev.twitch.bot.server.port.secondary;

import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;

public interface EventPublisher {

	void publish(Exception ex);

	void publish(ChannelFetchedAllEvent event);

	void publish(ChannelCreatedEvent event);

	void publish(ChannelUpdatedEvent event);

	void publish(ChannelDeletedEvent event);

	void publish(RaidConfigurationFetchedAllEvent event);

	void publish(RaidConfigurationFetchedEvent event);

	void publish(RaidConfigurationUpdatedEvent event);

	void publish(AuthenticatedStatusEvent event);

}
