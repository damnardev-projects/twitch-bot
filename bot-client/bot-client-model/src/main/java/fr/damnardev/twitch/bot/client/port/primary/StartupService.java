package fr.damnardev.twitch.bot.client.port.primary;

import java.util.function.Consumer;
import java.util.function.Function;

public interface StartupService {

	<T> void run(T instance, Consumer<T> closeMethod, Function<T, Function<Class<?>, Object>> getBeanMethod);

}
