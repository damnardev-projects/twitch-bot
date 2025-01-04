package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.server.model.form.UpdateChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.channel.CreateChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.UpdateChannelService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChannelController {

	private final FetchAllChannelService fetchAllChannelService;

	private final CreateChannelService createChannelService;

	private final UpdateChannelService updateChannelService;

	@MessageMapping("/channels/fetchAll")
	public void fetchAll() {
		this.fetchAllChannelService.fetchAll();
	}

	@MessageMapping("/channels/create")
	public void create(CreateChannelForm form) {
		this.createChannelService.create(form);
	}

	@MessageMapping("/channels/update")
	public void update(UpdateChannelForm form) {
		this.updateChannelService.update(form);
	}

}
