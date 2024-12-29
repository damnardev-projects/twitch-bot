package fr.damnardev.twitch.bot.server.port.secondary.command;

import java.util.Optional;

import fr.damnardev.twitch.bot.server.model.Channel;
import fr.damnardev.twitch.bot.server.model.Command;

public interface FindChannelCommandRepository {

	Optional<Command> findByChannelAndName(Channel channel, String name);

}
