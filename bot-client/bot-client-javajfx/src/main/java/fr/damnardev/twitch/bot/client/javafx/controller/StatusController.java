package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.DomainService;
import fr.damnardev.twitch.bot.client.model.event.ErrorEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class StatusController {

	public static final String ERROR_STR = "Error: %s";

	private static final String ERROR_STYLE = "Error";

	@FXML
	private Label labelStatus;

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

}
