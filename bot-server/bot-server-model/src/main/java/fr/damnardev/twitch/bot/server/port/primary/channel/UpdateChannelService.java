package fr.damnardev.twitch.bot.server.port.primary.channel;

import fr.damnardev.twitch.bot.server.model.form.UpdateChannelForm;

public interface UpdateChannelService {

	void update(UpdateChannelForm form);

}
