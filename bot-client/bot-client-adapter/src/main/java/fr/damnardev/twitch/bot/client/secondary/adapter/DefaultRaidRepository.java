package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.StompSessionWriter;
import fr.damnardev.twitch.bot.client.port.secondary.RaidRepository;
import fr.damnardev.twitch.bot.model.form.UpdateRaidConfigurationForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultRaidRepository implements RaidRepository {

	private final StompSessionWriter stompSessionWriter;

	@Override
	public void fetch(String name) {
		this.stompSessionWriter.send("/request/raids/fetch", name);
	}

	@Override
	public void update(UpdateRaidConfigurationForm form) {
		this.stompSessionWriter.send("/request/raids/update", form);
	}

}
