package fr.damnardev.twitch.bot.client.port.primary;

import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;

public interface ChannelService {

	void fetchAll(ChannelFetchedAllEvent event);

}
