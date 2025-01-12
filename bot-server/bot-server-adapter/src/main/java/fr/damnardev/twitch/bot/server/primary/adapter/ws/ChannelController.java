package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.server.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.server.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.server.model.form.UpdateChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.channel.CreateChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.DeleteChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.UpdateChannelService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChannelController {

	private final FetchAllChannelService fetchAllChannelService;

	private final CreateChannelService createChannelService;

	private final UpdateChannelService updateChannelService;

	private final DeleteChannelService deleteChannelService;

	private final ThreadPoolTaskExecutor executor;

	@MessageMapping("/channels/fetchAll")
	public void fetchAll() {
		this.executor.execute(this.fetchAllChannelService::fetchAll);
	}

	@MessageMapping("/channels/create")
	public void create(CreateChannelForm form) {
		this.executor.execute(() -> this.createChannelService.create(form));
	}

	@MessageMapping("/channels/update")
	public void update(UpdateChannelForm form) {
		this.executor.execute(() -> this.updateChannelService.update(form));
	}

	@MessageMapping("/channels/delete")
	public void delete(DeleteChannelForm form) {
		this.executor.execute(() -> this.deleteChannelService.delete(form));
	}

}
