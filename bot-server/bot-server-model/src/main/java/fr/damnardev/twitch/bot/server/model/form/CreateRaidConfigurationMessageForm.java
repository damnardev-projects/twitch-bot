package fr.damnardev.twitch.bot.server.model.form;

import lombok.Builder;

@Builder
public record CreateRaidConfigurationMessageForm(Long channelId, String channelName,
		String message) {
}
