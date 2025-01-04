package fr.damnardev.twitch.bot.server.port.secondary;

import fr.damnardev.twitch.bot.server.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.server.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.server.model.event.Event;
import fr.damnardev.twitch.bot.server.model.event.RaidConfigurationFetchedAllEvent;

public interface EventPublisher {

	<T extends Event> void publish(T event);

	void publish(ChannelFetchedAllEvent event);

	void publish(ChannelCreatedEvent event);

	void publish(ChannelUpdatedEvent event);

	void publish(RaidConfigurationFetchedAllEvent event);

}
