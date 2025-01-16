package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;

public interface CommandInterpreter {

	void interpret(Channel channel, GlobalCommand globalCommand, ChannelMessageEventForm form);

	GlobalCommandType getCommandTypeInterpreter();

}
