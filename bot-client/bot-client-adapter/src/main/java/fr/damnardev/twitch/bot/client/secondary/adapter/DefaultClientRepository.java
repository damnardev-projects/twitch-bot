package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionStorage;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultClientRepository implements ClientRepository {

	private final StompSessionStorage stompSessionStorage;

	private Runnable callback;

	@Override
	public void setCallback(Runnable callback) {
		this.callback = callback;
	}

	@Override
	public void connect() {
		if (this.callback != null) {
			this.callback.run();
		}
	}

	@Override
	public void fetchAuthenticationStatus() {
		if (this.stompSessionStorage.getSession() != null) {
			this.stompSessionStorage.getSession().send("/request/status/fetch", null);
		}
	}

}
