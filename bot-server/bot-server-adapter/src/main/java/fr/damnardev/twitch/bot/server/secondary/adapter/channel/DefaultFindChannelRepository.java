package fr.damnardev.twitch.bot.server.secondary.adapter.channel;

import java.util.List;
import java.util.Optional;

import fr.damnardev.twitch.bot.server.database.repository.DbChannelRepository;
import fr.damnardev.twitch.bot.server.model.Channel;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.secondary.mapper.ChannelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultFindChannelRepository implements FindChannelRepository {

	private final DbChannelRepository dbChannelRepository;

	private final ChannelMapper channelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<Channel> findAllEnabled() {
		return this.dbChannelRepository.findAllEnabled().stream().map(this.channelMapper::toModel).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Channel> findByName(String name) {
		return this.dbChannelRepository.findByName(name).map(this.channelMapper::toModel);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Channel> findAll() {
		return this.dbChannelRepository.findAll().stream().map(this.channelMapper::toModel).toList();
	}

}
