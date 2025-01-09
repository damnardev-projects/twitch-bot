package fr.damnardev.twitch.bot.server.model.event;

import lombok.Builder;

@Builder
public record AuthenticatedStatusEvent(Boolean value) {
}
