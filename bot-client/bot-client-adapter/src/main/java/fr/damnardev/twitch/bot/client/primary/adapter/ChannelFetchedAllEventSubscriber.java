package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChannelFetchedAllEventSubscriber implements Subscriber<ChannelFetchedAllEvent> {

	private final ApplicationService applicationService;

	@Override
	public String getDestination() {
		return "/response/channels/fetchedAll";
	}

	@Override
	public Class<ChannelFetchedAllEvent> getPayloadType() {
		return ChannelFetchedAllEvent.class;
	}

	@Override
	public void handleEvent(StompHeaders headers, ChannelFetchedAllEvent payload) {
		this.applicationService.handleChannelFetchedAllEvent(payload);
	}

}
