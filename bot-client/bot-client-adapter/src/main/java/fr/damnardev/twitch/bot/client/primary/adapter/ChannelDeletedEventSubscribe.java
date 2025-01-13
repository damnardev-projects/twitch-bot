package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChannelDeletedEventSubscribe implements Subscriber<ChannelDeletedEvent> {

	private final ApplicationService applicationService;

	@Override
	public String getDestination() {
		return "/response/channels/deleted";
	}

	@Override
	public Class<ChannelDeletedEvent> getPayloadType() {
		return ChannelDeletedEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, ChannelDeletedEvent payload) {
		this.applicationService.handleChannelDeletedEvent(payload);
	}

}
