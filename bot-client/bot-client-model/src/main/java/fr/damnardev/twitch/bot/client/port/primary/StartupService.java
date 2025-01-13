package fr.damnardev.twitch.bot.client.port.primary;

import java.util.function.Function;

public interface StartupService {

	void run(Runnable closeMethod, Function<Class<?>, Object> controllerFactory);

}
