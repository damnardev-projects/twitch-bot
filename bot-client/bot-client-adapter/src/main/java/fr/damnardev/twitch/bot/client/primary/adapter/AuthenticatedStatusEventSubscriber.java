package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticatedStatusEventSubscriber implements Subscriber<AuthenticatedStatusEvent> {

	private final StatusService statusService;

	@Override
	public String getDestination() {
		return "/response/authenticated/status";
	}

	@Override
	public Class<AuthenticatedStatusEvent> getPayloadType() {
		return AuthenticatedStatusEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, AuthenticatedStatusEvent payload) {
		this.statusService.handleAuthenticationStatus(payload);
	}
}
