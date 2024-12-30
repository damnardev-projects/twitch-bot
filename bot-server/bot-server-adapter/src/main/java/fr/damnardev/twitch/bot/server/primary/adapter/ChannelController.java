package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChannelController {

	private final FetchAllChannelService fetchAllChannelService;

	@MessageMapping("/channels/fetchAll")
	public void fetchAll() {
		fetchAllChannelService.fetchAll();
	}

}
