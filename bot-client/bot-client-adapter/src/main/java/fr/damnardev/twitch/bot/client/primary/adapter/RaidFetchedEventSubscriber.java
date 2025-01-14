package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RaidFetchedEventSubscriber implements Subscriber<RaidConfigurationFetchedEvent> {

	private final ApplicationService applicationService;

	@Override
	public String getDestination() {
		return "/response/raids/fetched";
	}

	@Override
	public Class<RaidConfigurationFetchedEvent> getPayloadType() {
		return RaidConfigurationFetchedEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, RaidConfigurationFetchedEvent payload) {
		this.applicationService.handleRaidConfigurationFetchedEvent(payload);
	}

}
