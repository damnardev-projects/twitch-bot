package fr.damnardev.twitch.bot.server.model.form;

import lombok.Builder;

@Builder(toBuilder = true)
public record ChannelMessageEventForm(Long channelId, String channelName, String sender,
		String message) {
}
