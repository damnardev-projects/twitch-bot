package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.exception.FatalException;
import fr.damnardev.twitch.bot.server.model.Command;
import fr.damnardev.twitch.bot.server.model.CommandType;
import fr.damnardev.twitch.bot.server.model.Message;
import fr.damnardev.twitch.bot.server.model.SuggestGame;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.primary.RandomService;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;
import fr.damnardev.twitch.bot.server.port.secondary.SuggestGameRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class SuggestGameInterpreter implements CommandInterpreter {

	private final SuggestGameRepository suggestGameRepository;

	private final MessageRepository messageRepository;

	private final RandomService randomService;

	@Override
	public void interpret(Channel channel, Command command, ChannelMessageEventForm form) {
		var suggestGame = SuggestGame.builder().viewer(form.sender()).game(form.message()).build();
		if (form.message() == null || form.message().isBlank()) {
			var content = String.format("/me usage: !%s nom_du_jeu", command.name());
			var message = Message.builder().channelId(channel.id()).channelName(channel.name()).content(content).build();
			this.messageRepository.sendMessage(message);
			throw new FatalException("No game suggested");
		}
		var result = this.suggestGameRepository.suggest(channel, suggestGame);
		if (result) {
			var value = this.randomService.getRandom(command.messages());
			var content = String.format("%s [⏰ %d s]", value, command.cooldown());
			var message = Message.builder().channelId(channel.id()).channelName(channel.name()).content(content).build();
			this.messageRepository.sendMessage(message);
		}
	}

	@Override
	public CommandType getCommandTypeInterpreter() {
		return CommandType.SUGGEST_GAME;
	}

}
