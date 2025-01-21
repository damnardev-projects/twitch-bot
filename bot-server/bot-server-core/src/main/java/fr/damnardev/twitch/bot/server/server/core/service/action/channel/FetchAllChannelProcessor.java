package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class FetchAllChannelProcessor implements Processor<Void> {

	private final FindChannelRepository findChannelRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.FETCH_ALL_CHANNEL;
	}

	@Override
	public void process(ActionForm<Void> form) {
		var channels = this.findChannelRepository.findAll();
		this.eventPublisher.publish(ChannelFetchedAllEvent.builder().value(channels).build());
	}

}
