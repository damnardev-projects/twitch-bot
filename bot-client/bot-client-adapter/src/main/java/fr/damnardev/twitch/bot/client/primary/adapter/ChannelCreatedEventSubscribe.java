package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChannelCreatedEventSubscribe implements Subscriber<ChannelCreatedEvent> {

	private final ApplicationService applicationService;

	@Override
	public String getDestination() {
		return "/response/channels/created";
	}

	@Override
	public Class<ChannelCreatedEvent> getPayloadType() {
		return ChannelCreatedEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, ChannelCreatedEvent payload) {
		this.applicationService.handleChannelCreatedEvent(payload);
	}

}
