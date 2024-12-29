package fr.damnardev.twitch.bot.client.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Shoutout(Long channelId, String channelName, Long raiderId,
		String raiderName) {
}
