package fr.damnardev.twitch.bot.client.secondary.adapter;

import fr.damnardev.twitch.bot.client.model.form.CreateRaidConfigurationMessageForm;
import fr.damnardev.twitch.bot.client.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.client.model.form.DeleteRaidConfigurationMessageForm;
import fr.damnardev.twitch.bot.client.model.form.UpdateChannelForm;
import fr.damnardev.twitch.bot.client.model.form.UpdateRaidConfigurationForm;
import fr.damnardev.twitch.bot.client.port.secondary.channel.DeleteChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.client.port.secondary.channel.UpdateChannelService;
import fr.damnardev.twitch.bot.client.port.secondary.raid.CreateRaidConfigurationMessageRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.DeleteRaidConfigurationMessageRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.FetchAllRaidConfigurationRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.FetchRaidConfigurationRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.UpdateRaidConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultRepository implements DeleteChannelRepository, FetchAllChannelService, UpdateChannelService,
		CreateRaidConfigurationMessageRepository, DeleteRaidConfigurationMessageRepository,
		FetchAllRaidConfigurationRepository, FetchRaidConfigurationRepository, UpdateRaidConfigurationRepository {

	@Override
	public void delete(DeleteChannelForm event) {

	}

	@Override
	public void fetchAll() {

	}

	@Override
	public void update(UpdateChannelForm event) {

	}

	@Override
	public void create(CreateRaidConfigurationMessageForm event) {

	}

	@Override
	public void delete(DeleteRaidConfigurationMessageForm event) {

	}

	@Override
	public void fetch(String event) {

	}

	@Override
	public void update(UpdateRaidConfigurationForm event) {

	}

}
