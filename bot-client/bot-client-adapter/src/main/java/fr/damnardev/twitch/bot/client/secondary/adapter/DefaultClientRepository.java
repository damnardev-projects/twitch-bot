package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionWriter;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.form.ActionForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultClientRepository implements ClientRepository {

	private final StompSessionWriter stompSessionWriter;

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
		this.stompSessionWriter.send(RepositoryConstants.PATH, ActionForm.FETCH_AUTHENTICATED.builder().build());
	}

}
