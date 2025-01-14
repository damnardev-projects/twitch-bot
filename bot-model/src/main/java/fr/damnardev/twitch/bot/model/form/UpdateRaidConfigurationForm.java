package fr.damnardev.twitch.bot.model.form;

import java.util.List;

import lombok.Builder;

@Builder
public record UpdateRaidConfigurationForm(Long channelId, String channelName,
		Boolean wizebotShoutoutEnabled, Boolean twitchShoutoutEnabled,
		Boolean raidMessageEnabled, List<String> messages) {
}
