package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.server.port.primary.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ClientController {

	private final AuthenticationService authenticationService;

	private final ThreadPoolTaskExecutor executor;

	@MessageMapping("/status/fetch")
	public void fetch() {
		this.executor.execute(this.authenticationService::isAuthenticated);
	}

}
