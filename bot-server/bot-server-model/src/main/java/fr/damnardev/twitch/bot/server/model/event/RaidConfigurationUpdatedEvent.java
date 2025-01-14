package fr.damnardev.twitch.bot.server.model.event;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
import lombok.Builder;

@Builder
public record RaidConfigurationUpdatedEvent(RaidConfiguration value) {
}
