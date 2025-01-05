package fr.damnardev.twitch.bot.client.port.secondary.channel;

import fr.damnardev.twitch.bot.client.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.client.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.client.model.form.UpdateChannelForm;

public interface ChannelRepository {

	void create(CreateChannelForm event);

	void update(UpdateChannelForm event);

	void delete(DeleteChannelForm form);
	
}
