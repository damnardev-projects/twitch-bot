package fr.damnardev.twitch.bot.server.server.core.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.primary.ChannelMessageEventService;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.command.FindChannelCommandRepository;
import fr.damnardev.twitch.bot.server.port.secondary.command.UpdateChannelGlobalCommandRepository;
import fr.damnardev.twitch.bot.server.server.core.service.command.CommandInterpreter;

@DomainService
public class DefaultChannelMessageEventService implements ChannelMessageEventService {

	private final DefaultTryService tryService;

	private final FindChannelRepository findChannelRepository;

	private final FindChannelCommandRepository channelCommandRepository;

	private final UpdateChannelGlobalCommandRepository updateChannelGlobalCommandRepository;

	private final Map<GlobalCommandType, CommandInterpreter> commandInterpreters;

	private final DateService dateService;

	public DefaultChannelMessageEventService(DefaultTryService tryService, FindChannelRepository findChannelRepository, FindChannelCommandRepository channelCommandRepository, UpdateChannelGlobalCommandRepository updateChannelGlobalCommandRepository, List<CommandInterpreter> commandInterpreters, DateService dateService) {
		this.tryService = tryService;
		this.findChannelRepository = findChannelRepository;
		this.channelCommandRepository = channelCommandRepository;
		this.updateChannelGlobalCommandRepository = updateChannelGlobalCommandRepository;
		this.commandInterpreters = (commandInterpreters != null) ? commandInterpreters.stream().collect(Collectors.toMap(CommandInterpreter::getCommandTypeInterpreter, Function.identity())) : new HashMap<>();
		this.dateService = dateService;
	}

	@Override
	public void message(ChannelMessageEventForm form) {
		this.tryService.doTry(this::doInternal, form);
	}

	private void doInternal(ChannelMessageEventForm form) {
		var optionalChannel = this.findChannelRepository.findByName(form.channelName());
		if (optionalChannel.isEmpty()) {
			throw new BusinessException("Channel not found");
		}
		var channel = optionalChannel.get();
		if (form.message().startsWith("!")) {
			var split = form.message().substring(1).split(" ", 2);
			var commandName = split[0];
			var parameter = (split.length == 2) ? split[1] : "";
			form = form.toBuilder().message(parameter).build();
			var optionalCommand = this.channelCommandRepository.findByChannelAndName(channel, commandName);
			if (optionalCommand.isEmpty()) {
				return;
			}
			var command = optionalCommand.get();
			var now = this.dateService.now();
			if (command.lastExecution() != null && command.lastExecution().plusSeconds(command.cooldown()).isAfter(now)) {
				return;
			}
			this.message(channel, command, form, now);
		}
	}

	private void message(Channel channel, GlobalCommand globalCommand, ChannelMessageEventForm form, OffsetDateTime now) {
		var commandInterpreter = this.commandInterpreters.get(globalCommand.type());
		if (commandInterpreter != null) {
			var updatedCommand = globalCommand.toBuilder().lastExecution(now).build();
			commandInterpreter.interpret(channel, globalCommand, form);
			this.updateChannelGlobalCommandRepository.update(updatedCommand);
		}
	}

}
