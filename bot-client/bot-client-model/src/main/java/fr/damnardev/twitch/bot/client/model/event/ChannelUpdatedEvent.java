package fr.damnardev.twitch.bot.client.model.event;

import fr.damnardev.twitch.bot.client.model.Channel;
import lombok.Builder;

@Builder
public record ChannelUpdatedEvent(Channel value) {
}
