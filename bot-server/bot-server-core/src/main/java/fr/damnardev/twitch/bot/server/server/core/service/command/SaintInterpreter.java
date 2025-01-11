package fr.damnardev.twitch.bot.server.server.core.service.command;


import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.server.model.Command;
import fr.damnardev.twitch.bot.server.model.CommandType;
import fr.damnardev.twitch.bot.server.model.Message;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;
import fr.damnardev.twitch.bot.server.port.secondary.SaintRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaintInterpreter implements CommandInterpreter {

	private final MessageRepository messageRepository;

	private final SaintRepository saintRepository;

	@Override
	public void interpret(Channel channel, Command command, ChannelMessageEventForm form) {
		var value = this.saintRepository.find();
		if (value.isPresent()) {
			var content = String.format("%s [⏰ %d s]", value.get(), command.cooldown());
			var message = Message.builder().channelId(channel.id()).channelName(channel.name()).content(content).build();
			this.messageRepository.sendMessage(message);
		}
	}

	public CommandType getCommandTypeInterpreter() {
		return CommandType.SAINT;
	}

}
