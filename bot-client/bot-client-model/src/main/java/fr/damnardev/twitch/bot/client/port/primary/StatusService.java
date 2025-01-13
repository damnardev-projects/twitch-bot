package fr.damnardev.twitch.bot.client.port.primary;

public interface StatusService {

	void handleConnectionStatus(Boolean status);

	void handleAuthenticationStatus(Boolean status);

}
