package fr.damnardev.twitch.bot.client.model.event;

import java.util.List;

import fr.damnardev.twitch.bot.client.model.Channel;
import lombok.Builder;

@Builder
public record ChannelFetchedAllEvent(List<Channel> value) {
}
