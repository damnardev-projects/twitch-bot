package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.DomainService;
import fr.damnardev.twitch.bot.client.model.event.ErrorEvent;
import fr.damnardev.twitch.bot.client.port.primary.StatusService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class StatusController implements StatusService {

	public static final String ERROR_STR = "Error: %s";

	private static final String ERROR_STYLE = "Error";

	private static final String DISCONNECTED_STYLE = "Disconnected";

	private static final String CONNECTED_STYLE = "Connected";

	@FXML
	private Label labelStatus;

	@FXML
	private Label labelConnected;

	public void setLabelText(String status, boolean isError) {
		this.labelStatus.setText(status);
		this.labelStatus.getStyleClass().remove(ERROR_STYLE);
		if (isError) {
			this.labelStatus.getStyleClass().add(ERROR_STYLE);
		}
	}

	public void onErrorEvent(ErrorEvent event) {
		log.error("Error has occurred", event.getException());
		this.setLabelText(ERROR_STR.formatted(event.getException().getMessage()), true);
	}

	public void setLabelConnected(String status, boolean isError) {
		if (this.labelConnected != null) {
			this.labelConnected.setText(status);
			if (isError) {
				this.labelConnected.getStyleClass().remove(CONNECTED_STYLE);
				this.labelConnected.getStyleClass().add(DISCONNECTED_STYLE);
			}
			else {
				this.labelConnected.getStyleClass().add(CONNECTED_STYLE);
				this.labelConnected.getStyleClass().remove(DISCONNECTED_STYLE);
			}
		}
	}

	@Override
	public void connected(Boolean status) {
		Platform.runLater(() -> {
			if (status == null || !status) {
				setLabelConnected("DISCONNECTED", true);
			}
			else {
				setLabelConnected("CONNECTED", false);
			}
		});
	}

}
