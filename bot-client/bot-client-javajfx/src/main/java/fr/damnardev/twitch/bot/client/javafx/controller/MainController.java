package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.ApplicationStartedEventListener;
import fr.damnardev.twitch.bot.client.port.primary.ApplicationService;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class MainController implements ApplicationService, StatusService {

	private final ClientRepository clientRepository;

	private final ChannelManagementController channelManagementController;

	private final ApplicationStartedEventListener applicationStartedEventListener;

	@FXML
	private ComboBox<String> channelComboBox;

	@FXML
	private Label twitchStatusLabel;

	@FXML
	private Label serverStatusLabel;

	@FXML
	private BorderPane borderPane;

	@Override
	public void handleConnectionStatus(Boolean status) {
		Platform.runLater(() -> {
			this.serverStatusLabel.setText(Boolean.TRUE.equals(status) ? "Connected" : "Disconnected");
			this.clientRepository.fetchAuthenticationStatus();
		});
	}

	@Override
	public void handleAuthenticationStatus(AuthenticatedStatusEvent status) {
		Platform.runLater(() -> this.twitchStatusLabel.setText(Boolean.TRUE.equals(status.value()) ? "Connected" : "Disconnected"));
	}

	@Override
	public void handleChannelFetchedAllEvent(ChannelFetchedAllEvent event) {
		Platform.runLater(() -> {
			this.channelComboBox.getItems().clear();
			this.channelComboBox.getItems().add("");
			this.channelComboBox.getItems().addAll(event.value().stream().map(Channel::name).sorted(String::compareToIgnoreCase).toList());
		});
		Platform.runLater(() -> this.channelManagementController.handleChannelFetchedAllEvent(event));
	}

	@Override
	public void handleChannelCreatedEvent(ChannelCreatedEvent event) {
		Platform.runLater(() -> {
			this.channelComboBox.getItems().add(event.value().name());
			this.channelComboBox.getItems().sorted(String::compareToIgnoreCase);
		});
		Platform.runLater(() -> this.channelManagementController.handlerChannelCreatedEvent(event));
	}

	@Override
	public void handleChannelUpdatedEvent(ChannelUpdatedEvent event) {
		Platform.runLater(() -> this.channelManagementController.handleChannelUpdatedEvent(event));
	}

	@Override
	public void handleChannelDeletedEvent(ChannelDeletedEvent event) {
		Platform.runLater(() -> this.channelComboBox.getItems().remove(event.value().name()));
		Platform.runLater(() -> this.channelManagementController.handleChannelDeletedEvent(event));
	}

	public void openDashboard() {
		this.borderPane.setCenter(this.applicationStartedEventListener.get("dashboard"));
	}

}
