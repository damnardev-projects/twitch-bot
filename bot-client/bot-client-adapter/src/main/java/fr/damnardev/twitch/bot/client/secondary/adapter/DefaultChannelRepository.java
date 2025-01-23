package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionWriter;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultChannelRepository implements ChannelRepository {

	private final StompSessionWriter stompSessionWriter;

	@Override
	public void fetchAll() {
		this.stompSessionWriter.send("/request/actions", ActionForm.FETCH_ALL_CHANNEL);
	}

	//
//	@Override
//	public void create(CreateChannelForm form) {
//		this.stompSessionWriter.send("/request/channels/create", form);
//	}
//
	@Override
	public void update(long channelId, boolean enabled) {
		this.stompSessionWriter.send("/request/actions", ActionForm.UPDATE_CHANNEL_ENABLED.builder().resourceId(channelId).value(enabled).build());
	}

	@Override
	public void delete(long channelId) {
		this.stompSessionWriter.send("/request/actions", ActionForm.DELETE_CHANNEL.builder().resourceId(channelId).build());
	}

}
