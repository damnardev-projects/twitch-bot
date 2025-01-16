package fr.damnardev.twitch.bot.server.secondary.adapter.command;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.server.database.entity.DbChannelGlobalCommand;
import fr.damnardev.twitch.bot.server.database.repository.DbChannelGlobalCommandRepository;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.port.secondary.command.FindChannelCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultFindChannelCommandRepository implements FindChannelCommandRepository {

	private final DbChannelGlobalCommandRepository dbChannelGlobalCommandRepository;

	@Override
	@Transactional(readOnly = true)
	public Optional<GlobalCommand> findByChannelAndName(Channel channel, String name) {
		return this.dbChannelGlobalCommandRepository.findByChannelNameAndName(channel.name(), name).map(this::toModel);
	}

	private GlobalCommand toModel(DbChannelGlobalCommand command) {
		return GlobalCommand.builder().channelId(command.getId()).channelName(command.getChannel().getName()).name(command.getName()).type(command.getType()).enabled(command.isEnabled()).cooldown(command.getCooldown()).lastExecution(command.getLastExecution()).build();

	}

}
