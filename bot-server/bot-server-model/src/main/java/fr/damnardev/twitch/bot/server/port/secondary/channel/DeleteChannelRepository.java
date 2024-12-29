package fr.damnardev.twitch.bot.server.port.secondary.channel;

import fr.damnardev.twitch.bot.server.model.Channel;

public interface DeleteChannelRepository {

	void delete(Channel channel);

}
