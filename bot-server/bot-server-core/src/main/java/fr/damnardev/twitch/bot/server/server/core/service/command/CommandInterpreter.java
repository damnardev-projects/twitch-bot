package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.server.model.Command;
import fr.damnardev.twitch.bot.server.model.CommandType;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;

public interface CommandInterpreter {

	void interpret(Channel channel, Command command, ChannelMessageEventForm form);

	CommandType getCommandTypeInterpreter();

}
