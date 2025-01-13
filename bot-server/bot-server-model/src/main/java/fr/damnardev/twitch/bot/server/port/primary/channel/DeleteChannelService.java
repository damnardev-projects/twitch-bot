package fr.damnardev.twitch.bot.server.port.primary.channel;

import fr.damnardev.twitch.bot.model.form.DeleteChannelForm;

public interface DeleteChannelService {

	void delete(DeleteChannelForm form);

}
