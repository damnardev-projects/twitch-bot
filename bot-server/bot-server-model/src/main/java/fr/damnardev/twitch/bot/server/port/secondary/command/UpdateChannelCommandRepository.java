package fr.damnardev.twitch.bot.server.port.secondary.command;

import fr.damnardev.twitch.bot.server.model.Command;

public interface UpdateChannelCommandRepository {

	void update(Command command);

}
