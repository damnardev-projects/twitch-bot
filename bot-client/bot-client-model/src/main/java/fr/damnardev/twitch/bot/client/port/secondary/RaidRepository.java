package fr.damnardev.twitch.bot.client.port.secondary;

import fr.damnardev.twitch.bot.model.form.UpdateRaidConfigurationForm;

public interface RaidRepository {

	void fetch(String name);

	void update(UpdateRaidConfigurationForm form);
	
}
