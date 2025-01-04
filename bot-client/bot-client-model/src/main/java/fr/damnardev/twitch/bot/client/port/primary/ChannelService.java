package fr.damnardev.twitch.bot.client.port.primary;

import fr.damnardev.twitch.bot.client.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelUpdatedEvent;

public interface ChannelService {

	void fetchAll(ChannelFetchedAllEvent event);

	void create(ChannelCreatedEvent event);

	void update(ChannelUpdatedEvent event);

}
