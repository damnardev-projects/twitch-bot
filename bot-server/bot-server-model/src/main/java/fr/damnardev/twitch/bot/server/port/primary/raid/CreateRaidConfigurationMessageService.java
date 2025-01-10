package fr.damnardev.twitch.bot.server.port.primary.raid;

import fr.damnardev.twitch.bot.server.model.form.CreateRaidConfigurationMessageForm;

public interface CreateRaidConfigurationMessageService {

	void create(CreateRaidConfigurationMessageForm form);

}
