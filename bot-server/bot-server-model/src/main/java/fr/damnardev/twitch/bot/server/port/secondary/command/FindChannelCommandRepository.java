package fr.damnardev.twitch.bot.server.port.secondary.command;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;

public interface FindChannelCommandRepository {

	Optional<GlobalCommand> findByChannelAndName(Channel channel, String name);

}
