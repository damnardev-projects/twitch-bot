package fr.damnardev.twitch.bot.server.port.secondary;

public interface AuthenticationRepository {

	boolean isInitialized();

	boolean isValid();

	boolean renew();

}
