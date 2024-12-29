package fr.damnardev.twitch.bot.client.port.secondary.raid;

import fr.damnardev.twitch.bot.client.model.form.DeleteRaidConfigurationMessageForm;

public interface DeleteRaidConfigurationMessageRepository {

	void delete(DeleteRaidConfigurationMessageForm event);

}
