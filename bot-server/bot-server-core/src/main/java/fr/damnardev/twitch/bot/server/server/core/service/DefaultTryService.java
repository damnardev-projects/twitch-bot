package fr.damnardev.twitch.bot.server.server.core.service;

import java.util.function.Consumer;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.server.port.primary.TryService;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DefaultTryService implements TryService {

	private final EventPublisher eventPublisher;

	@Override
	public void doTry(Runnable runnable) {
		try {
			runnable.run();
		}
		catch (Exception ex) {
			this.eventPublisher.publish(ex);
		}
	}

	@Override
	public <T> void doTry(Consumer<T> consumer, T t) {
		try {
			consumer.accept(t);
		}
		catch (Exception ex) {
			this.eventPublisher.publish(ex);
		}
	}

}
