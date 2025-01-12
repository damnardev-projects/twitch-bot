package fr.damnardev.twitch.bot.server.server.core.service;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class DefaultDateService implements DateService {

	@Override
	public OffsetDateTime now() {
		return OffsetDateTime.now();
	}

	@Override
	public OffsetDateTime now(ZoneId zoneId) {
		return OffsetDateTime.now(zoneId);
	}

}
