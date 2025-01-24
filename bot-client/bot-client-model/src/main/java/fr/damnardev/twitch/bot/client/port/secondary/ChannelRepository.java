package fr.damnardev.twitch.bot.client.port.secondary;

public interface ChannelRepository {

	void fetchAll();

	void create(String channel);

	void update(long channelId, boolean enabled);

	//
	void delete(long channelId);

}
