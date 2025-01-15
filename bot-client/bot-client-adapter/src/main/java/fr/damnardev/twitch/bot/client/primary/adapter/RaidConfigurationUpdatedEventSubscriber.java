package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RaidConfigurationUpdatedEventSubscriber implements Subscriber<RaidConfigurationUpdatedEvent> {

	private final ApplicationService applicationService;

	@Override
	public String getDestination() {
		return "/response/raids/updated";
	}

	@Override
	public Class<RaidConfigurationUpdatedEvent> getPayloadType() {
		return RaidConfigurationUpdatedEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, RaidConfigurationUpdatedEvent payload) {
		this.applicationService.handleRaidConfigurationUpdatedEvent(payload);
	}

}
