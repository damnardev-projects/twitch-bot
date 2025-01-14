package fr.damnardev.twitch.bot.model.event;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
import lombok.Builder;

@Builder
public record RaidConfigurationFetchedEvent(RaidConfiguration value) {
}
