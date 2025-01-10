package fr.damnardev.twitch.bot.server.port.primary;

import fr.damnardev.twitch.bot.server.model.form.ChannelRaidEventForm;

public interface ChannelRaidEventService {

	void raid(ChannelRaidEventForm form);

}
