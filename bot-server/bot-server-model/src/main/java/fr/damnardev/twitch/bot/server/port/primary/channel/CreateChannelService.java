package fr.damnardev.twitch.bot.server.port.primary.channel;

import fr.damnardev.twitch.bot.model.form.CreateChannelForm;

public interface CreateChannelService {

	void create(CreateChannelForm form);

}
