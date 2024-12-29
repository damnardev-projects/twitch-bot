package fr.damnardev.twitch.bot.client.port.secondary.channel;

import fr.damnardev.twitch.bot.client.model.form.CreateChannelForm;

public interface CreateChannelRepository {

	void create(CreateChannelForm event);

}
