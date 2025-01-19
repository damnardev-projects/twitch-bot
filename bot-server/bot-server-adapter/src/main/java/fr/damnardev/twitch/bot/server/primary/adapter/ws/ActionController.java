package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.primary.action.ActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ActionController {

	private final ActionService actionService;

	private final ThreadPoolTaskExecutor executor;

	@MessageMapping("/actions")
	public <T> void action(ActionForm<T> form) {
		log.info("Received action: {}", form);
		this.executor.execute(() -> this.actionService.process(form));
	}

}
