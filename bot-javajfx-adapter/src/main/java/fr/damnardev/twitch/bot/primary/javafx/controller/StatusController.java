package fr.damnardev.twitch.bot.primary.javafx.controller;

import fr.damnardev.twitch.bot.domain.model.event.ErrorEvent;
import fr.damnardev.twitch.bot.primary.javafx.adapter.ApplicationStartedEventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(ApplicationStartedEventListener.class)
public class StatusController {

	public static final String ERROR_STR = "Error: %s";

	@FXML
	private Label labelStatus;

	public void setLabelText(String status, boolean isError) {
		this.labelStatus.setText(status);
		if (isError) {
			this.labelStatus.setStyle("-fx-text-fill: red;");
		}
		else {
			this.labelStatus.setStyle("");
		}
	}

	public void onErrorEvent(ErrorEvent event) {
		log.error("Error has occurred", event.getException());
		this.setLabelText(ERROR_STR.formatted(event.getException().getMessage()), true);
	}

}
