package fr.damnardev.twitch.bot.server.primary.adapter.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.primary.ChannelMessageEventService;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ChannelMessageEventConsumer extends AbstractChannelEventConsumer<ChannelMessageEvent, ChannelMessageEventForm> {

	public ChannelMessageEventConsumer(TwitchClient twitchClient, ThreadPoolTaskExecutor executor, ChannelMessageEventService handler) {
		super(executor, twitchClient, handler::message, ChannelMessageEvent.class);
	}

	@Override
	protected ChannelMessageEventForm toModel(ChannelMessageEvent event) {
		var eventUser = event.getUser();
		var eventChannel = event.getChannel();
		var channelId = Long.parseLong(eventChannel.getId());
		var channelName = eventChannel.getName();
		return ChannelMessageEventForm.builder().channelId(channelId).channelName(channelName).sender(eventUser.getName()).message(event.getMessage()).build();
	}

}
