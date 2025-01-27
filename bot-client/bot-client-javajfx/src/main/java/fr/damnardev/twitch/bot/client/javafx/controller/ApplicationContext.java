package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.wrapper.ObservableChannel;
import fr.damnardev.twitch.bot.client.javafx.wrapper.ObservableRaidConfiguration;
import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.RaidRepository;
import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.RaidConfiguration;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationUpdatedEvent;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class ApplicationContext implements ApplicationService {

	@Getter
	private final ObservableList<ObservableChannel> channels = FXCollections.observableArrayList();

	private final ChannelRepository channelRepository;

	private final RaidRepository raidRepository;

	@Getter
	private ObservableChannel selectedChannel;

	private boolean disabledChannelUpdate = true;

	private boolean disabledRaidUpdate = true;

	@Getter
	private final ObservableRaidConfiguration raidConfiguration = buildObservableRaidConfiguration();

	public void setSelectedChannel(ObservableChannel selectedChannel) {
		this.selectedChannel = selectedChannel;
		if (selectedChannel != null) {
			this.raidRepository.fetch(selectedChannel.getId().getValue());
		}
	}

	@Override
	public void handleChannelFetchedAllEvent(ChannelFetchedAllEvent event) {
		Platform.runLater(() -> {
			this.channels.clear();
			this.channels.addAll(event.value().stream().map(this::buildObservableChannel).toList());
		});
	}

	@Override
	public void handleChannelCreatedEvent(ChannelCreatedEvent event) {
		Platform.runLater(() -> this.channels.add(buildObservableChannel(event.value())));
	}

	@Override
	public void handleChannelUpdatedEvent(ChannelUpdatedEvent event) {
		Platform.runLater(() -> {
			var channel = event.value();
			var wrapper = this.channels.stream().filter((item) -> item.getId().getValue().equals(channel.id())).findFirst().orElseThrow();
			this.disabledChannelUpdate = false;
			wrapper.getEnabled().set(channel.enabled());
			wrapper.getOnline().set(channel.online());
			this.disabledChannelUpdate = true;
		});
	}

	@Override
	public void handleChannelDeletedEvent(ChannelDeletedEvent event) {
		Platform.runLater(() -> {
			var channel = event.value();
			var wrapper = this.channels.stream().filter((item) -> item.getId().getValue().equals(channel.id())).findFirst().orElseThrow();
			this.channels.remove(wrapper);
		});
	}

	@Override
	public void handleRaidConfigurationFetchedEvent(RaidConfigurationFetchedEvent payload) {
		Platform.runLater(() -> {
			if (this.selectedChannel != null && this.selectedChannel.getName().getValue().equals(payload.value().channelName())) {
				this.disabledRaidUpdate = false;
				this.raidConfiguration.update(payload.value());
				this.disabledRaidUpdate = true;
			}
		});
	}

	@Override
	public void handleRaidConfigurationUpdatedEvent(RaidConfigurationUpdatedEvent payload) {
		Platform.runLater(() -> {
			if (this.selectedChannel != null && this.selectedChannel.getName().getValue().equals(payload.value().channelName())) {
				this.disabledRaidUpdate = false;
				this.raidConfiguration.update(payload.value());
				this.disabledRaidUpdate = true;
			}
		});
	}

	@Override
	public void connected() {
		this.channelRepository.fetchAll();
		if (this.selectedChannel != null) {
			this.raidRepository.fetch(this.selectedChannel.getId().getValue());
		}
	}

	private ObservableChannel buildObservableChannel(Channel channel) {
		var wrapper = new ObservableChannel(channel);
		wrapper.addListener(this::update);
		return wrapper;
	}

	private void update(ObservableValue<? extends Channel> observableValue, Channel oldValue, Channel newValue) {
		if (!this.disabledChannelUpdate) {
			return;
		}
		this.channelRepository.update(newValue.id(), newValue.enabled());
	}

	private ObservableRaidConfiguration buildObservableRaidConfiguration() {
		var wrapper = new ObservableRaidConfiguration();
		wrapper.addListener(this::update);
		return wrapper;
	}

	private void update(ObservableValue<? extends RaidConfiguration> observableValue, RaidConfiguration oldValue, RaidConfiguration newValue) {
		if (!this.disabledRaidUpdate) {
			return;
		}
		if (oldValue.twitchShoutoutEnabled() != newValue.twitchShoutoutEnabled()) {
			this.raidRepository.updateTwitchShoutout(newValue.channelId(), newValue.twitchShoutoutEnabled());
		}
	}

}
