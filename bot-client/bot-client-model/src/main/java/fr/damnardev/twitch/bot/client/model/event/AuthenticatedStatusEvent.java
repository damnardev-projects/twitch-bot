package fr.damnardev.twitch.bot.client.model.event;

import lombok.Builder;

@Builder
public record AuthenticatedStatusEvent(Boolean value) {
}
