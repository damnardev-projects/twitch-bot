package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionWriter;
import fr.damnardev.twitch.bot.client.port.secondary.RaidRepository;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultRaidRepository implements RaidRepository {

	private final StompSessionWriter stompSessionWriter;

	@Override
	public void fetch(long channelId) {
		this.stompSessionWriter.send(RepositoryConstants.PATH, ActionForm.FETCH_RAID.builder().resourceId(channelId).build());
	}

	@Override
	public void updateTwitchShoutout(long channelId, boolean value) {
		this.stompSessionWriter.send(RepositoryConstants.PATH, ActionForm.UPDATE_RAID_TWITCH_SHOUTOUT.builder().resourceId(channelId).value(value).build());
	}

	@Override
	public void updateWizebotShoutout(long channelId, boolean value) {
		this.stompSessionWriter.send(RepositoryConstants.PATH, ActionForm.UPDATE_RAID_WIZEBOT_SHOUTOUT.builder().resourceId(channelId).value(value).build());
	}

	@Override
	public void updateRaidMessageEnabled(long channelId, boolean value) {
		this.stompSessionWriter.send(RepositoryConstants.PATH, ActionForm.UPDATE_RAID_MESSAGE_ENABLED.builder().resourceId(channelId).value(value).build());
	}

//	@Override
//	public void update(ActionFor form) {
//		this.stompSessionWriter.send("/request/raids/update", form);
//	}

}
