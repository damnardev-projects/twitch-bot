package fr.damnardev.twitch.bot.server.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Message(Long channelId, String channelName, String content) {
}
