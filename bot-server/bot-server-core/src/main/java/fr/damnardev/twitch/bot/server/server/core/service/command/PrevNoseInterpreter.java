package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.server.model.CommandType;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;

public class PrevNoseInterpreter extends NoseInterpreter {

	public PrevNoseInterpreter(MessageRepository messageRepository, DateService dateService) {
		super(messageRepository, dateService);
	}

	@Override
	protected boolean isForward() {
		return false;
	}

	@Override
	public CommandType getCommandTypeInterpreter() {
		return CommandType.PREV_NOSE;
	}

}
