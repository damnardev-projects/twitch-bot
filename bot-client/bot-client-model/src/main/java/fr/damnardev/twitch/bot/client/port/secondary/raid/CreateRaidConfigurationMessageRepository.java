package fr.damnardev.twitch.bot.client.port.secondary.raid;

import fr.damnardev.twitch.bot.client.model.form.CreateRaidConfigurationMessageForm;

public interface CreateRaidConfigurationMessageRepository {

	void create(CreateRaidConfigurationMessageForm event);

}
