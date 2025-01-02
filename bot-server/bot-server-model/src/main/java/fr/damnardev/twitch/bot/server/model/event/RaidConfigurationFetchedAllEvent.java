package fr.damnardev.twitch.bot.server.model.event;

import java.util.List;

import fr.damnardev.twitch.bot.server.model.RaidConfiguration;
import lombok.Builder;

@Builder
public record RaidConfigurationFetchedAllEvent(List<RaidConfiguration> value) {
}
