package fr.damnardev.twitch.bot.server.server.core.service.action.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.SaveChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CreateChannelProcessor implements Processor<String> {

	private final FindChannelRepository findChannelRepository;

	private final SaveChannelRepository saveChannelRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.CREATE_CHANNEL;
	}

	@Override
	public void process(ActionForm<String> form) {
		if (form.getValue() == null) {
			throw new BusinessException("Channel name is required");
		}
		if (this.findChannelRepository.findByName(form.getValue()).isPresent()) {
			throw new BusinessException("Channel already exists");
		}
		var channel = this.saveChannelRepository.save(form.getValue());
		this.eventPublisher.publish(ChannelCreatedEvent.builder().value(channel).build());
	}

}
