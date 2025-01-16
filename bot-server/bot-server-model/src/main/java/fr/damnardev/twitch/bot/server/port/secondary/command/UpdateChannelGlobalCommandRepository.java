package fr.damnardev.twitch.bot.server.port.secondary.command;

import fr.damnardev.twitch.bot.server.model.GlobalCommand;

public interface UpdateChannelGlobalCommandRepository {

	void update(GlobalCommand globalCommand);

}
