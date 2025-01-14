package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.wrapper.ChannelWrapper;
import fr.damnardev.twitch.bot.client.javafx.wrapper.RaidConfigurationWrapper;
import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.RaidRepository;
import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.model.form.UpdateChannelForm;
import javafx.application.Platform;
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
	private final ObservableList<ChannelWrapper> channels = FXCollections.observableArrayList();

	@Getter
	private final RaidConfigurationWrapper raidConfiguration = new RaidConfigurationWrapper();

	private final ChannelRepository channelRepository;

	private final RaidRepository raidRepository;

	@Getter
	private ChannelWrapper selectedChannel;

	public void setSelectedChannel(ChannelWrapper selectedChannel) {
		this.selectedChannel = selectedChannel;
		if (selectedChannel != null) {
			this.raidRepository.fetch(selectedChannel.nameProperty().getValue());
		}
	}

	@Override
	public void handleChannelFetchedAllEvent(ChannelFetchedAllEvent event) {
		Platform.runLater(() -> {
			this.channels.clear();
			this.channels.addAll(event.value().stream().map(this::buildWrapper).toList());
		});
	}

	@Override
	public void handleChannelCreatedEvent(ChannelCreatedEvent event) {
		Platform.runLater(() -> this.channels.add(buildWrapper(event.value())));
	}

	@Override
	public void handleChannelUpdatedEvent(ChannelUpdatedEvent event) {
		Platform.runLater(() -> {
			var channel = event.value();
			var wrapper = this.channels.stream().filter((item) -> item.idProperty().getValue().equals(channel.id())).findFirst().orElseThrow();
			wrapper.enabledProperty().set(channel.enabled());
			wrapper.onlineProperty().set(channel.online());
		});
	}

	@Override
	public void handleChannelDeletedEvent(ChannelDeletedEvent event) {
		Platform.runLater(() -> {
			var channel = event.value();
			var wrapper = this.channels.stream().filter((item) -> item.idProperty().getValue().equals(channel.id())).findFirst().orElseThrow();
			this.channels.remove(wrapper);
		});
	}

	@Override
	public void handleRaidConfigurationFetchedEvent(RaidConfigurationFetchedEvent payload) {
		Platform.runLater(() -> {
			var raidConfiguration = payload.value();
			this.raidConfiguration.update(raidConfiguration);
		});
	}

	@Override
	public void connected() {
		this.channelRepository.fetchAll();
		if (this.selectedChannel != null) {
			this.raidRepository.fetch(this.selectedChannel.nameProperty().getValue());
		}
	}

	private ChannelWrapper buildWrapper(Channel channel) {
		var channelWrapper = new ChannelWrapper(channel);
		channelWrapper.enabledProperty().addListener((observable, oldValue, newValue) -> {
			var form = UpdateChannelForm.builder().id(channel.id()).name(channel.name()).enabled(newValue).build();
			this.channelRepository.update(form);
		});
		return channelWrapper;
	}

}
