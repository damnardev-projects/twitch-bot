package fr.damnardev.twitch.bot.server.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record SuggestGame(String viewer, String game) {
}
