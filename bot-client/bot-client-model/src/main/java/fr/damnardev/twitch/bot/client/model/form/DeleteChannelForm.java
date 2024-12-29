package fr.damnardev.twitch.bot.client.model.form;

import lombok.Builder;

@Builder
public record DeleteChannelForm(Long id, String name) {
}
