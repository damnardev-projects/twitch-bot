package fr.damnardev.twitch.bot.server.port.secondary.channel;

import java.util.List;

import fr.damnardev.twitch.bot.server.model.Channel;

public interface UpdateChannelRepository {

	void update(Channel channel);

	void updateAll(List<Channel> channels);

}
