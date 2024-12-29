package fr.damnardev.twitch.bot.server.model.form;

import lombok.Builder;

@Builder
public record UpdateRaidConfigurationForm(Long channelId, String channelName,
		Boolean wizebotShoutoutEnabled, Boolean twitchShoutoutEnabled,
		Boolean raidMessageEnabled) {
}
