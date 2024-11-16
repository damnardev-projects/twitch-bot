package fr.damnardev.twitch.bot.domain.model.form;

import lombok.Builder;

@Builder
public record UpdateChannelForm(Long id, String name, Boolean enabled, Boolean online) {
}
