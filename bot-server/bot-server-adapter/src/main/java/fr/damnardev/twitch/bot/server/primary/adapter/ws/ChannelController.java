package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.model.form.UpdateChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.channel.CreateChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.DeleteChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.UpdateChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
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
		log.info("Received create channel request: {}", form);
		this.executor.execute(() -> this.createChannelService.create(form));
	}

	@MessageMapping("/channels/update")
	public void update(UpdateChannelForm form) {
		log.info("Received update channel request: {}", form);
		this.executor.execute(() -> this.updateChannelService.update(form));
	}

	@MessageMapping("/channels/delete")
	public void delete(DeleteChannelForm form) {
		log.info("Received delete channel request: {}", form);
		this.executor.execute(() -> this.deleteChannelService.delete(form));
	}

}
