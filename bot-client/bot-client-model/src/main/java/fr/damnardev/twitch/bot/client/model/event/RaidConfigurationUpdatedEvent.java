package fr.damnardev.twitch.bot.client.model.event;

import fr.damnardev.twitch.bot.client.model.RaidConfiguration;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Value
public class RaidConfigurationUpdatedEvent extends Event<RaidConfiguration> {

	@Builder
	public RaidConfigurationUpdatedEvent(RaidConfiguration raidConfiguration) {
		super(raidConfiguration);
	}

	@Override
	public boolean hasError() {
		return false;
	}

}