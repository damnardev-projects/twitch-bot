package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChannelUpdatedEventSubscribe implements Subscriber<ChannelUpdatedEvent> {

	private final ApplicationService applicationService;

	@Override
	public String getDestination() {
		return "/response/channels/updated";
	}

	@Override
	public Class<ChannelUpdatedEvent> getPayloadType() {
		return ChannelUpdatedEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, ChannelUpdatedEvent payload) {
		this.applicationService.handleChannelUpdatedEvent(payload);
	}

}
