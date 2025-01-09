package fr.damnardev.twitch.bot.server.port.secondary;

import fr.damnardev.twitch.bot.server.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.server.model.event.Event;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationUpdatedEvent;

public interface EventPublisher {

	<T extends Event> void publish(T event);

	void publish(ChannelFetchedAllEvent event);

	void publish(ChannelCreatedEvent event);

	void publish(ChannelUpdatedEvent event);

	void publish(ChannelDeletedEvent event);

	void publish(RaidConfigurationFetchedAllEvent event);

	void publish(RaidConfigurationUpdatedEvent event);

	void publish(AuthenticatedStatusEvent event);
	
}
