package fr.damnardev.twitch.bot.client.model.event;

import java.util.List;

import fr.damnardev.twitch.bot.client.model.RaidConfiguration;
import lombok.Builder;

@Builder
public record RaidConfigurationFetchedAllEvent(List<RaidConfiguration> value) {
}

