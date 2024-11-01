package fr.damnardev.twitch.bot.secondary.adapter;

import fr.damnardev.twitch.bot.database.repository.DbChannelRepository;
import fr.damnardev.twitch.bot.domain.model.Channel;
import fr.damnardev.twitch.bot.domain.port.secondary.DeleteChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultDeleteChannelRepository implements DeleteChannelRepository {

	private final DbChannelRepository dbChannelRepository;

	@Override
	public void delete(Channel channel) {
		log.info("Deleting channel {}", channel.name());
		this.dbChannelRepository.deleteById(channel.id());
		log.info("Deleted channel {}", channel.name());
	}

}