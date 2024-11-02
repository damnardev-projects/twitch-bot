package fr.damnardev.twitch.bot.domain.port.primary;

import java.util.function.Consumer;

public interface TryService {

	void doTry(Runnable runnable);

	<T> void doTry(Consumer<T> consumer, T t);

}
