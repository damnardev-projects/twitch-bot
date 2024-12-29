package fr.damnardev.twitch.bot.server.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Channel(Long id, String name, boolean enabled, boolean online) {

	public boolean isOffline() {
		return !this.enabled || !this.online;
	}

}
