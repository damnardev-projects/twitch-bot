package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.model.exception.FatalException;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.model.Message;
import fr.damnardev.twitch.bot.server.model.SuggestGame;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;
import fr.damnardev.twitch.bot.server.port.secondary.SuggestGameRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class SuggestGameInterpreter implements CommandInterpreter {

	private final SuggestGameRepository suggestGameRepository;

	private final MessageRepository messageRepository;

	@Override
	public void interpret(Channel channel, GlobalCommand globalCommand, ChannelMessageEventForm form) {
		var suggestGame = SuggestGame.builder().viewer(form.sender()).game(form.message()).build();
		if (form.message() == null || form.message().isBlank()) {
			var content = String.format("/me usage: !%s nom_du_jeu", globalCommand.name());
			var message = Message.builder().channelId(channel.id()).channelName(channel.name()).content(content).build();
			this.messageRepository.sendMessage(message);
			throw new FatalException("No game suggested");
		}
		var result = this.suggestGameRepository.suggest(channel, suggestGame);
		if (result) {
			var content = String.format("%s [⏰ %d s]", "Merci pour la suggestion du jeu", globalCommand.cooldown());
			var message = Message.builder().channelId(channel.id()).channelName(channel.name()).content(content).build();
			this.messageRepository.sendMessage(message);
		}
	}

	@Override
	public GlobalCommandType getCommandTypeInterpreter() {
		return GlobalCommandType.SUGGEST_GAME;
	}

}
