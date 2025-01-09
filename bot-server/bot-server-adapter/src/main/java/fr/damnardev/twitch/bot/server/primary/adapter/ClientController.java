package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.port.primary.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ClientController {

	private final AuthenticationService authenticationService;

	@MessageMapping("/status/fetch")
	public void fetchAll() {
		this.authenticationService.isAuthenticated();
	}

}
