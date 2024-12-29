package fr.damnardev.twitch.bot.client.port.secondary.raid;

import fr.damnardev.twitch.bot.client.model.form.UpdateRaidConfigurationForm;

public interface UpdateRaidConfigurationRepository {

	void update(UpdateRaidConfigurationForm event);

}
