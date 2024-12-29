package fr.damnardev.twitch.bot.server.port.secondary;

import fr.damnardev.twitch.bot.server.model.Shoutout;

public interface ShoutoutRepository {

	void sendShoutout(Shoutout shoutout);

}
