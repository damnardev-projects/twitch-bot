package fr.damnardev.twitch.bot.server.server.core.service;

import java.util.function.Consumer;

import fr.damnardev.twitch.bot.server.DomainService;
import fr.damnardev.twitch.bot.server.model.event.ErrorEvent;
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
			var event = ErrorEvent.builder().exception(ex).build();
			this.eventPublisher.publish(event);
		}
	}

	@Override
	public <T> void doTry(Consumer<T> consumer, T t) {
		try {
			consumer.accept(t);
		}
		catch (Exception ex) {
			var event = ErrorEvent.builder().exception(ex).build();
			this.eventPublisher.publish(event);
		}
	}

}
