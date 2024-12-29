package fr.damnardev.twitch.bot.server.port.secondary;

import java.util.List;

import fr.damnardev.twitch.bot.server.model.Channel;

public interface ChatRepository {

	void joinAll(List<Channel> channels);

	void join(Channel channel);

	void reconnect();

	void leave(Channel channel);

}
