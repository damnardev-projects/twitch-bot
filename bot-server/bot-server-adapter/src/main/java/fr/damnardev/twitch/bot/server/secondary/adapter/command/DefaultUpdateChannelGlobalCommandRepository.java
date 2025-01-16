package fr.damnardev.twitch.bot.server.secondary.adapter.command;

import fr.damnardev.twitch.bot.server.database.repository.DbChannelGlobalCommandRepository;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.port.secondary.command.UpdateChannelGlobalCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultUpdateChannelGlobalCommandRepository implements UpdateChannelGlobalCommandRepository {

	private final DbChannelGlobalCommandRepository dbChannelGlobalCommandRepository;

	@Override
	@Transactional
	public void update(GlobalCommand globalCommand) {
		log.info("Updating command {}", globalCommand.name());
		this.dbChannelGlobalCommandRepository.findByChannelNameAndName(globalCommand.channelName(), globalCommand.name()).ifPresent((dbCommand) -> {
			dbCommand.setEnabled(globalCommand.enabled());
			dbCommand.setType(globalCommand.type());
			dbCommand.setCooldown(globalCommand.cooldown());
			dbCommand.setLastExecution(globalCommand.lastExecution());
			this.dbChannelGlobalCommandRepository.save(dbCommand);
		});
		log.info("Updated command {}", globalCommand.name());
	}

}
