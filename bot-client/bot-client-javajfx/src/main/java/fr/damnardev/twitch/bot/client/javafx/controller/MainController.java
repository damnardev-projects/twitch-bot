package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.port.primary.ChannelService;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@DomainService
public class MainController implements StatusService, ChannelService {

	private final ClientRepository clientRepository;

	@FXML
	public ComboBox<String> channelComboBox;

	@FXML
	public Label twitchStatusLabel;

	@FXML
	public Label serverStatusLabel;

	@Override
	public void connected(Boolean status) {
		Platform.runLater(() -> {
			if (this.serverStatusLabel != null) {
				this.serverStatusLabel.setText(status ? "Connected" : "Disconnected");
				this.clientRepository.fetchTwitchStatus();
			}
		});
	}

	@Override
	public void authenticated(Boolean status) {
		Platform.runLater(() -> {
			if (this.twitchStatusLabel != null) {
				this.twitchStatusLabel.setText(status ? "Connected" : "Disconnected");
			}
		});
	}

	@Override
	public void fetchAll(ChannelFetchedAllEvent event) {
		Platform.runLater(() -> {
			if (this.channelComboBox != null) {
				this.channelComboBox.getItems().clear();
				this.channelComboBox.getItems().addAll(event.value().stream().map(Channel::name).sorted(String::compareToIgnoreCase).toList());
			}
		});
	}

}
