package fr.damnardev.twitch.bot.server.port.primary;

import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;

public interface ChannelMessageEventService {

	void message(ChannelMessageEventForm form);

}
