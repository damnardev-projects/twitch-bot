package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionStorage;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.model.form.UpdateChannelForm;
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

	@Override
	public void create(CreateChannelForm form) {
		if (this.stompSessionStorage.getSession() != null) {
			this.stompSessionStorage.getSession().send("/request/channels/create", form);
		}
	}

	@Override
	public void update(UpdateChannelForm form) {
		if (this.stompSessionStorage.getSession() != null) {
			this.stompSessionStorage.getSession().send("/request/channels/update", form);
		}
	}

	@Override
	public void delete(DeleteChannelForm form) {
		if (this.stompSessionStorage.getSession() != null) {
			this.stompSessionStorage.getSession().send("/request/channels/delete", form);
		}
	}

}
