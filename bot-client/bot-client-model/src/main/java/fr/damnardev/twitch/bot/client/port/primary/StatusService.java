package fr.damnardev.twitch.bot.client.port.primary;

import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;

public interface StatusService {

	void handleConnectionStatus(Boolean status);

	void handleAuthenticationStatus(AuthenticatedStatusEvent status);

}
