package fr.damnardev.twitch.bot.client.port.secondary.channel;

import fr.damnardev.twitch.bot.client.model.form.UpdateChannelForm;

public interface UpdateChannelService {

	void update(UpdateChannelForm event);

}
