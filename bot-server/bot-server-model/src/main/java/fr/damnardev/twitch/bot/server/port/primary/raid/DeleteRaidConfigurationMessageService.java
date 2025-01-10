package fr.damnardev.twitch.bot.server.port.primary.raid;

import fr.damnardev.twitch.bot.server.model.form.DeleteRaidConfigurationMessageForm;

public interface DeleteRaidConfigurationMessageService {

	void delete(DeleteRaidConfigurationMessageForm form);

}
