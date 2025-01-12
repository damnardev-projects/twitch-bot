package fr.damnardev.twitch.bot.server.server.core.service.channel;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.server.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.server.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.TryService;
import fr.damnardev.twitch.bot.server.port.primary.channel.CreateChannelService;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.SaveChannelRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class DefaultCreateChannelService implements CreateChannelService {

	private final TryService tryService;

	private final FindChannelRepository findChannelRepository;

	private final SaveChannelRepository saveChannelRepository;

	private final EventPublisher eventPublisher;

	@Override
	public void create(CreateChannelForm form) {
		this.tryService.doTry(this::doInternal, form);
	}

	private void doInternal(CreateChannelForm form) {
		var name = form.name();
		var optionalChannel = this.findChannelRepository.findByName(name);
		if (optionalChannel.isPresent()) {
			throw new BusinessException("Channel already exists");
		}
		var channel = Channel.builder().name(name).build();
		channel = this.saveChannelRepository.save(channel);
		var event = ChannelCreatedEvent.builder().value(channel).build();
		this.eventPublisher.publish(event);
	}

}
