package fr.damnardev.twitch.bot.server.port.secondary;

import fr.damnardev.twitch.bot.server.model.Channel;
import fr.damnardev.twitch.bot.server.model.SuggestGame;

public interface SuggestGameRepository {

	boolean suggest(Channel channel, SuggestGame suggestGame);

}
