package fr.damnardev.twitch.bot.client.port.secondary;

public interface ClientRepository {

	void setCallback(Runnable callback);

	void connect();

	void fetchTwitchStatus();

}
