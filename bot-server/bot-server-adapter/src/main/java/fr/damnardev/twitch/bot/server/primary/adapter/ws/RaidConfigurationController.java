package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.server.model.form.UpdateRaidConfigurationForm;
import fr.damnardev.twitch.bot.server.port.primary.raid.FetchAllRaidConfigurationService;
import fr.damnardev.twitch.bot.server.port.primary.raid.FetchRaidConfigurationService;
import fr.damnardev.twitch.bot.server.port.primary.raid.UpdateRaidConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Controller
@Service
public class RaidConfigurationController {

	private final FetchAllRaidConfigurationService fetchAllRaidConfigurationService;

	private final FetchRaidConfigurationService fetchRaidConfigurationService;

	private final UpdateRaidConfigurationService updateRaidConfigurationService;

	private final ThreadPoolTaskExecutor executor;

	@MessageMapping("/raids/fetchAll")
	public void fetchAll() {
		log.info("Received fetch all raids request");
		this.executor.execute(this.fetchAllRaidConfigurationService::fetchAll);
	}

	@MessageMapping("/raids/fetch")
	public void fetch(String name) {
		log.info("Received fetch all raid request: {}", name);
		this.executor.execute(() -> this.fetchRaidConfigurationService.fetch(name));
	}

	@MessageMapping("/raids/update")
	public void update(UpdateRaidConfigurationForm form) {
		log.info("Received update raid request: {}", form);
		this.executor.execute(() -> this.updateRaidConfigurationService.update(form));
	}

}
