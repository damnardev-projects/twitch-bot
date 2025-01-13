package fr.damnardev.twitch.bot.model.event;

import fr.damnardev.twitch.bot.model.Channel;
import lombok.Builder;

@Builder
public record ChannelDeletedEvent(Channel value) {
}
