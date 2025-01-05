package fr.damnardev.twitch.bot.server.port.primary.channel;

import fr.damnardev.twitch.bot.server.model.form.DeleteChannelForm;

public interface DeleteChannelService {

	void delete(DeleteChannelForm form);

}
