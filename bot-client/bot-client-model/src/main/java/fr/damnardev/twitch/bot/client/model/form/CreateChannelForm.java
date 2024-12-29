package fr.damnardev.twitch.bot.client.model.form;

import lombok.Builder;

@Builder
public record CreateChannelForm(String name) {
}
