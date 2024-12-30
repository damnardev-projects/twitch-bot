package fr.damnardev.twitch.bot.server.model.event;

import java.util.List;

import fr.damnardev.twitch.bot.server.model.Channel;
import lombok.Builder;

@Builder
public record ChannelFetchedAllEvent(List<Channel> value) {
}
