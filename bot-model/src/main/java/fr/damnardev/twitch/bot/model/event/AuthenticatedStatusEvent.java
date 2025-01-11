package fr.damnardev.twitch.bot.model.event;

import lombok.Builder;

@Builder
public record AuthenticatedStatusEvent(Boolean value) {
}
