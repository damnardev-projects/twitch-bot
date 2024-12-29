package fr.damnardev.twitch.bot.server.port.primary;

public interface EventService<M> {

	void process(M event);

}
