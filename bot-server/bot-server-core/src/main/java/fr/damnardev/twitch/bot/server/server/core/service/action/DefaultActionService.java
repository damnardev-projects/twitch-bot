package fr.damnardev.twitch.bot.server.server.core.service.action;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.exception.FatalException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.primary.TryService;
import fr.damnardev.twitch.bot.server.port.primary.action.ActionService;

@DomainService
public class DefaultActionService implements ActionService {

	private final Map<ActionKey, Processor<?>> processors;

	private final TryService tryService;

	public DefaultActionService(Set<Processor<?>> processors, TryService tryService) {
		this.tryService = tryService;
		this.processors = processors.stream().collect(Collectors.toMap(Processor::getActionKey, Function.identity()));
	}

	@Override
	public <T> void process(ActionForm<T> form) {
		this.tryService.doTry(this::doInternal, form);
	}

	@SuppressWarnings("unchecked")
	private <T> void doInternal(ActionForm<T> form) {
		var processor = this.processors.get(form.getKey());
		if (processor == null) {
			throw new FatalException("No processor found for key " + form.getKey());
		}
		((Processor<T>) processor).process(form);
	}

}
