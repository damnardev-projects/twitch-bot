package fr.damnardev.twitch.bot.server.port.primary;

public interface AuthenticationService {

	boolean isInitialized();

	void tryRenew();

}
