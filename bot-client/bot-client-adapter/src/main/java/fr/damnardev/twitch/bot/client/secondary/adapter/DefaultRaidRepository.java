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

//	@Override
//	public void update(ActionFor form) {
//		this.stompSessionWriter.send("/request/raids/update", form);
//	}

}
