package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.UpdateChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ChannelOnlineProcessor implements Processor<Boolean> {

	private final FindChannelRepository findChannelRepository;

	private final UpdateChannelRepository updateChannelRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.UPDATE_CHANNEL_ONLINE;
	}

	@Override
	public void process(ActionForm<Boolean> form) {
		this.findChannelRepository.findById(form.getResourceId()).ifPresentOrElse((channel) -> {
			channel = channel.toBuilder().online(form.getValue()).build();
			this.updateChannelRepository.update(channel);
			this.eventPublisher.publish(ChannelUpdatedEvent.builder().value(channel).build());
		}, () -> {
			throw new BusinessException("Channel not found");
		});
	}

}
