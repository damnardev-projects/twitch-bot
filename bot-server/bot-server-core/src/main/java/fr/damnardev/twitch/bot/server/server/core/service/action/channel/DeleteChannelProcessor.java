package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.DeleteChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DeleteChannelProcessor implements Processor<Void> {

	private final FindChannelRepository findChannelRepository;

	private final DeleteChannelRepository deleteChannelRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.DELETE_CHANNEL;
	}

	@Override
	public void process(ActionForm<Void> form) {
		this.findChannelRepository.findById(form.getResourceId()).ifPresentOrElse((channel) -> {
			this.deleteChannelRepository.delete(channel);
			this.eventPublisher.publish(ChannelDeletedEvent.builder().value(channel).build());
		}, () -> {
			throw new BusinessException("Channel not found");
		});
	}

}
