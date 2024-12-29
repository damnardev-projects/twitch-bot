package fr.damnardev.twitch.bot.server.model.form;

import lombok.Builder;

@Builder
public record CreateChannelForm(String name) {
}
