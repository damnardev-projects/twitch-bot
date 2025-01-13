package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionWriter;
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

	private final StompSessionWriter stompSessionWriter;

	@Override
	public void fetchAll() {
		this.stompSessionWriter.send("/request/channels/fetchAll");
	}

	@Override
	public void create(CreateChannelForm form) {
		this.stompSessionWriter.send("/request/channels/create", form);
	}

	@Override
	public void update(UpdateChannelForm form) {
		this.stompSessionWriter.send("/request/channels/update", form);
	}

	@Override
	public void delete(DeleteChannelForm form) {
		this.stompSessionWriter.send("/request/channels/delete", form);
	}

}
