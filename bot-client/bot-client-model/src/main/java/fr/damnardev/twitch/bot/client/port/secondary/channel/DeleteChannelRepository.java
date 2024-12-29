package fr.damnardev.twitch.bot.client.port.secondary.channel;

import fr.damnardev.twitch.bot.client.model.form.DeleteChannelForm;

public interface DeleteChannelRepository {

	void delete(DeleteChannelForm event);

}
