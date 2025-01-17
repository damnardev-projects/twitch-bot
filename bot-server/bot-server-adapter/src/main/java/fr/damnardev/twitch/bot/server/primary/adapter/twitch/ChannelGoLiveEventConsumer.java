package fr.damnardev.twitch.bot.server.primary.adapter.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.primary.action.ActionService;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ChannelGoLiveEventConsumer extends AbstractChannelEventConsumer<ChannelGoLiveEvent, ActionForm<Boolean>> {

	public ChannelGoLiveEventConsumer(TwitchClient twitchClient, ThreadPoolTaskExecutor executor, ActionService handler) {
		super(executor, twitchClient, handler::process, ChannelGoLiveEvent.class);
	}

	@Override
	protected ActionForm<Boolean> toModel(ChannelGoLiveEvent event) {
		var channel = event.getChannel();
		var id = Long.parseLong(channel.getId());
		return ActionForm.UPDATE_RAID_ONLINE.builder().resourceId(id).value(true).build();
	}

}
