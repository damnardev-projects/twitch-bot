package fr.damnardev.twitch.bot.server.secondary.mapper;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.server.database.entity.DbChannel;

import org.springframework.stereotype.Service;

@Service
public class ChannelMapper {

	public Channel toModel(DbChannel dbChannel) {
		return Channel.builder()
				.id(dbChannel.getId())
				.name(dbChannel.getName())
				.online(dbChannel.isOnline())
				.enabled(dbChannel.isEnabled())
				.build();
	}

	public DbChannel toEntity(Channel channel) {
		return DbChannel.builder()
				.id(channel.id())
				.name(channel.name())
				.online(channel.online())
				.enabled(channel.enabled())
				.build();
	}
}
