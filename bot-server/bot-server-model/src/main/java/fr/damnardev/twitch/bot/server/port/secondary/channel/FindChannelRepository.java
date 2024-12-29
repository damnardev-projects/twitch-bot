package fr.damnardev.twitch.bot.server.port.secondary.channel;

import java.util.List;
import java.util.Optional;

import fr.damnardev.twitch.bot.server.model.Channel;

public interface FindChannelRepository {

	List<Channel> findAllEnabled();

	Optional<Channel> findByName(String name);

	List<Channel> findAll();

}
