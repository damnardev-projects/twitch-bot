package fr.damnardev.twitch.bot.server.server.core.service.channel;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.server.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.server.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.channel.DeleteChannelService;
import fr.damnardev.twitch.bot.server.port.secondary.ChatRepository;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.DeleteChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.server.core.service.DefaultTryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class DefaultDeleteChannelService implements DeleteChannelService {

	private final DefaultTryService tryService;

	private final FindChannelRepository findChannelRepository;

	private final DeleteChannelRepository deleteChannelRepository;

	private final ChatRepository chatRepository;

	private final EventPublisher eventPublisher;

	@Override
	public void delete(DeleteChannelForm form) {
		this.tryService.doTry(this::doInternal, form);
	}

	private void doInternal(DeleteChannelForm form) {
		var optionalChannel = this.findChannelRepository.findByName(form.name());
		if (optionalChannel.isEmpty()) {
			throw new BusinessException("Channel not found");
		}
		var channel = optionalChannel.get();
		if (channel.enabled()) {
			this.chatRepository.leave(channel);
		}
		this.deleteChannelRepository.delete(channel);
		var event = ChannelDeletedEvent.builder().value(channel).build();
		this.eventPublisher.publish(event);
	}

}
