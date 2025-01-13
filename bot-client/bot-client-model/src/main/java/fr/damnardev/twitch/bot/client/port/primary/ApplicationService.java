package fr.damnardev.twitch.bot.client.port.primary;

import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;

public interface ApplicationService {

	void handleChannelFetchedAllEvent(ChannelFetchedAllEvent event);

	void handlerChannelCreatedEvent(ChannelCreatedEvent event);

	void handleChannelUpdatedEvent(ChannelUpdatedEvent event);

	void handleChannelDeletedEvent(ChannelDeletedEvent event);

}
