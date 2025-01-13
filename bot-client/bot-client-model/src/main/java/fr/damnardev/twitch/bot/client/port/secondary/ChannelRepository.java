package fr.damnardev.twitch.bot.client.port.secondary;

import fr.damnardev.twitch.bot.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.model.form.UpdateChannelForm;

public interface ChannelRepository {

	void fetchAll();

	void create(CreateChannelForm form);

	void update(UpdateChannelForm form);

	void delete(DeleteChannelForm form);

}
