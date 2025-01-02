package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.port.primary.raid.FetchAllRaidConfigurationService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class RaidConfigurationController {

	private final FetchAllRaidConfigurationService fetchAllRaidConfigurationService;

	@MessageMapping("/raids/fetchAll")
	public void fetchAll() {
		this.fetchAllRaidConfigurationService.fetchAll();
	}

}
