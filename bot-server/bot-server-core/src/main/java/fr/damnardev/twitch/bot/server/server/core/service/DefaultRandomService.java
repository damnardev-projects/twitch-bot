package fr.damnardev.twitch.bot.server.server.core.service;

import java.util.List;
import java.util.Random;

import fr.damnardev.twitch.bot.server.port.primary.RandomService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRandomService implements RandomService {

	private final Random random;

	@Override
	public <T> T getRandom(List<T> values) {
		int index = this.random.nextInt(values.size());
		return values.get(index);
	}

}
