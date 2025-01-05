package fr.damnardev.twitch.bot.server.model.event;

import fr.damnardev.twitch.bot.server.model.Channel;
import lombok.Builder;

@Builder
public record ChannelDeletedEvent(Channel value) {
}
