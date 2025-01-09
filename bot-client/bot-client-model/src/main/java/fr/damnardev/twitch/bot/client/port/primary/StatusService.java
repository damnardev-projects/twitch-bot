package fr.damnardev.twitch.bot.client.port.primary;

public interface StatusService {

	void connected(Boolean status);

	void authenticated(Boolean status);

}
