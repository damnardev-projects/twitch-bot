package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.server.model.CommandType;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;

@DomainService
public class NextNoseInterpreter extends NoseInterpreter {

	public NextNoseInterpreter(MessageRepository messageRepository, DateService dateService) {
		super(messageRepository, dateService);
	}

	@Override
	protected boolean isForward() {
		return true;
	}

	@Override
	public CommandType getCommandTypeInterpreter() {
		return CommandType.NEXT_NOSE;
	}

}
