package fr.damnardev.twitch.bot.server.port.secondary;

import fr.damnardev.twitch.bot.server.model.Message;

public interface MessageRepository {

	void sendMessage(Message message);

}
