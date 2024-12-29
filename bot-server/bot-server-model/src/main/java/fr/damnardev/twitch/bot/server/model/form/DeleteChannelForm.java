package fr.damnardev.twitch.bot.server.model.form;

import lombok.Builder;

@Builder
public record DeleteChannelForm(Long id, String name) {
}
