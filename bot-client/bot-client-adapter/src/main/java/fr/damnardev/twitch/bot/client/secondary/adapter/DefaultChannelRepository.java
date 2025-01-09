package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionStorage;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultChannelRepository implements ChannelRepository {

	private final StompSessionStorage stompSessionStorage;

	@Override
	public void fetchAll() {
		if (this.stompSessionStorage.getSession() != null) {
			this.stompSessionStorage.getSession().send("/request/channels/fetchAll", null);
		}
	}

}
