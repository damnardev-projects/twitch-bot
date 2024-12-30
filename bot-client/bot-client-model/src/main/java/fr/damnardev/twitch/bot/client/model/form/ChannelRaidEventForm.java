package fr.damnardev.twitch.bot.client.model.form;

import lombok.Builder;

@Builder
public record ChannelRaidEventForm(Long channelId, String channelName, Long raiderId,
		String raiderName) {
}