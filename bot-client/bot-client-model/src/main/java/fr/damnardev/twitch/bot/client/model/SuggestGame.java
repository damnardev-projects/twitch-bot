package fr.damnardev.twitch.bot.client.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record SuggestGame(String viewer, String game) {
}
