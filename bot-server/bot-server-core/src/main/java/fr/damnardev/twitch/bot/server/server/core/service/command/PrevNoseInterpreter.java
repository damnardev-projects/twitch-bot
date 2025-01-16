package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;

@DomainService
public class PrevNoseInterpreter extends NoseInterpreter {

	public PrevNoseInterpreter(MessageRepository messageRepository, DateService dateService) {
		super(messageRepository, dateService);
	}

	@Override
	protected boolean isForward() {
		return false;
	}

	@Override
	public GlobalCommandType getCommandTypeInterpreter() {
		return GlobalCommandType.PREV_NOSE;
	}

}
