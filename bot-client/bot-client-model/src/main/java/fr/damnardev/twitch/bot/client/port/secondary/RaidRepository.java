package fr.damnardev.twitch.bot.client.port.secondary;

public interface RaidRepository {

	void fetch(long channelId);

	void updateTwitchShoutout(long channelId, boolean value);

	void updateWizebotShoutout(long channelId, boolean value);

	void updateRaidMessageEnabled(long channelId, boolean value);

}
