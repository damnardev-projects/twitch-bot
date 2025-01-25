package fr.damnardev.twitch.bot.server.server.core.service.action.raid;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class FetchRaidConfigurationProcessor implements Processor<Void> {

	private final FindRaidConfigurationRepository findRaidConfigurationRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.FETCH_RAID;
	}

	@Override
	public void process(ActionForm<Void> form) {
		this.findRaidConfigurationRepository.findByChannelId(form.getResourceId())
				.ifPresentOrElse(
						(raidConfiguration) -> this.eventPublisher.publish(RaidConfigurationFetchedEvent.builder().value(raidConfiguration).build()),
						() -> {
							throw new BusinessException("Raid Configuration not found");
						}
				);
	}

}
