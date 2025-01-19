package fr.damnardev.twitch.bot.server.primary.adapter.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.server.port.primary.action.ActionService;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ChannelGoOfflineEventConsumer extends AbstractChannelEventConsumer<ChannelGoOfflineEvent, ActionForm<Boolean>> {

	public ChannelGoOfflineEventConsumer(TwitchClient twitchClient, ThreadPoolTaskExecutor executor, ActionService handler) {
		super(executor, twitchClient, handler::process, ChannelGoOfflineEvent.class);
	}

	@Override
	protected ActionForm<Boolean> toModel(ChannelGoOfflineEvent event) {
		var channel = event.getChannel();
		var id = Long.parseLong(channel.getId());
		return ActionForm.UPDATE_RAID_ONLINE.builder().resourceId(id).value(false).build();
	}

}
