package fr.damnardev.twitch.bot.server.server.core.service.action.authenticated;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.AuthenticationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.server.core.service.action.ResourceProcessor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class AuthenticatedResourceProcessor implements ResourceProcessor<Void> {

	private final AuthenticationRepository authenticationRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.FETCH_AUTHENTICATED;
	}

	@Override
	public void process(ActionForm<Void> form) {
		var authenticated = this.authenticationRepository.isValid();
		this.eventPublisher.publish(AuthenticatedStatusEvent.builder().value(authenticated).build());
	}

}
