package fr.damnardev.twitch.bot.server.server.core.service.action;

import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;

public interface ResourceProcessor<T> {

	ActionKey getActionKey();

	void process(ActionForm<T> form);

}
