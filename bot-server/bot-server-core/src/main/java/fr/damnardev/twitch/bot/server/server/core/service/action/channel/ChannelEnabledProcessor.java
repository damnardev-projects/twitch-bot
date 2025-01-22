package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.StreamRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.UpdateChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ChannelEnabledProcessor implements Processor<Boolean> {

	private final FindChannelRepository findChannelRepository;

	private final UpdateChannelRepository updateChannelRepository;

	private final StreamRepository streamRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.UPDATE_CHANNEL_ENABLED;
	}

	@Override
	public void process(ActionForm<Boolean> form) {
		this.findChannelRepository.findById(form.getResourceId()).ifPresentOrElse((channel) -> {
			var online = false;
			var enabled = form.getValue();
			if (enabled) {
				online = this.streamRepository.computeOnline(channel).online();
			}
			channel = channel.toBuilder().enabled(enabled).online(online).build();
			this.updateChannelRepository.update(channel);
			this.eventPublisher.publish(ChannelUpdatedEvent.builder().value(channel).build());
		}, () -> {
			throw new BusinessException("Channel not found");
		});
	}

}
