package fr.damnardev.twitch.bot.client.javafx.controller;

import java.util.Comparator;

import fr.damnardev.twitch.bot.client.javafx.ApplicationStartedEventListener;
import fr.damnardev.twitch.bot.client.javafx.wrapper.ChannelWrapper;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.AuthenticatedStatusEvent;
import javafx.application.Platform;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class MainController implements StatusService {

	private final ClientRepository clientRepository;

	private final ApplicationStartedEventListener applicationStartedEventListener;

	private final ApplicationContext applicationContext;

	@FXML
	private ComboBox<ChannelWrapper> channelComboBox;

	@FXML
	private Label twitchStatusLabel;

	@FXML
	private Label serverStatusLabel;

	@FXML
	private BorderPane borderPane;

	@FXML
	public void initialize() {
		var sortedList = new SortedList<>(this.applicationContext.getChannels());
		sortedList.setComparator(Comparator.comparing((a) -> a.nameProperty().getValue(), String.CASE_INSENSITIVE_ORDER));
		this.channelComboBox.setItems(sortedList);
		this.channelComboBox.setConverter(new javafx.util.StringConverter<>() {

			@Override
			public String toString(ChannelWrapper channelWrapper) {
				return (channelWrapper != null) ? channelWrapper.nameProperty().getValue() : "";
			}

			@Override
			public ChannelWrapper fromString(String string) {
				return null;
			}

		});
	}

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

	public void openDashboard() {
		this.borderPane.setCenter(this.applicationStartedEventListener.get("dashboard"));
	}

	public void onItemSelected(ActionEvent actionEvent) {
		this.applicationContext.setSelectedChannel(this.channelComboBox.getSelectionModel().getSelectedItem());
	}

	public void openRaid(ActionEvent actionEvent) {
		this.borderPane.setCenter(this.applicationStartedEventListener.get("raid"));
	}

}
