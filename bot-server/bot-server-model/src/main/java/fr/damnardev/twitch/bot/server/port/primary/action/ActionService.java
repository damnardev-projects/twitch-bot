package fr.damnardev.twitch.bot.server.port.primary.action;

import fr.damnardev.twitch.bot.model.form.ActionForm;

public interface ActionService {

	<T> void process(ActionForm<T> form);

}
