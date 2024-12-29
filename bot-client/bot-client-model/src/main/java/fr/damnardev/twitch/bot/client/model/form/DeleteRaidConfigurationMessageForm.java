package fr.damnardev.twitch.bot.client.model.form;

import lombok.Builder;

@Builder
public record DeleteRaidConfigurationMessageForm(Long channelId, String channelName,
		String message) {
}
