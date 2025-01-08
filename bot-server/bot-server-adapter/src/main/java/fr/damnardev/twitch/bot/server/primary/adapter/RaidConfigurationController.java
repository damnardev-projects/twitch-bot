package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.model.form.UpdateRaidConfigurationForm;
import fr.damnardev.twitch.bot.server.port.primary.raid.FetchAllRaidConfigurationService;
import fr.damnardev.twitch.bot.server.port.primary.raid.UpdateRaidConfigurationService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class RaidConfigurationController {

	private final FetchAllRaidConfigurationService fetchAllRaidConfigurationService;

	private final UpdateRaidConfigurationService updateRaidConfigurationService;

	@MessageMapping("/raids/fetchAll")
	public void fetchAll() {
		this.fetchAllRaidConfigurationService.fetchAll();
	}

	@MessageMapping("/raids/update")
	public void update(UpdateRaidConfigurationForm form) {
		this.updateRaidConfigurationService.update(form);
	}

}
