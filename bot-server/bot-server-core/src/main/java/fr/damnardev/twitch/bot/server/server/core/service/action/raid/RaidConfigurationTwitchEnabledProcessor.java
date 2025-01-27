package fr.damnardev.twitch.bot.server.server.core.service.action.raid;

import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;
import fr.damnardev.twitch.bot.model.exception.BusinessException;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import fr.damnardev.twitch.bot.model.form.ActionKey;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.raid.FindRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.port.secondary.raid.UpdateRaidConfigurationRepository;
import fr.damnardev.twitch.bot.server.server.core.service.action.Processor;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class RaidConfigurationTwitchEnabledProcessor implements Processor<Boolean> {

	private final FindRaidConfigurationRepository findRaidConfigurationRepository;

	private final UpdateRaidConfigurationRepository updateRaidConfigurationRepository;

	private final EventPublisher eventPublisher;

	@Override
	public ActionKey getActionKey() {
		return ActionKey.UPDATE_RAID_TWITCH_SHOUTOUT;
	}

	@Override
	public void process(ActionForm<Boolean> form) {
		this.findRaidConfigurationRepository.findByChannelId(form.getResourceId()).ifPresentOrElse((raidConfiguration) -> {
			raidConfiguration = raidConfiguration.toBuilder().twitchShoutoutEnabled(form.getValue()).build();
			this.updateRaidConfigurationRepository.update(raidConfiguration);
			this.eventPublisher.publish(RaidConfigurationUpdatedEvent.builder().value(raidConfiguration).build());
		}, () -> {
			throw new BusinessException("Raid Configuration not found");
		});
	}

}
