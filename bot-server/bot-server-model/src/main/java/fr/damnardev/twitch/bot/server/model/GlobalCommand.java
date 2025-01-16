package fr.damnardev.twitch.bot.server.model;

import java.time.OffsetDateTime;

import fr.damnardev.twitch.bot.model.GlobalCommandType;
import lombok.Builder;

@Builder(toBuilder = true)
public record GlobalCommand(Long channelId, String channelName, String name,
		boolean enabled,
		long cooldown, OffsetDateTime lastExecution, GlobalCommandType type) {
}
