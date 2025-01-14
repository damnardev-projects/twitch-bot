package fr.damnardev.twitch.bot.server.port.primary;

import java.util.function.Consumer;

public interface TryService {

	void doTry(Runnable runnable);

	<T> void doTry(Consumer<T> consumer, T t);

}
