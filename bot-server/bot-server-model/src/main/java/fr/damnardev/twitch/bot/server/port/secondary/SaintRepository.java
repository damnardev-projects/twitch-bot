package fr.damnardev.twitch.bot.server.port.secondary;

import java.util.Optional;

public interface SaintRepository {

	Optional<String> find();

}
