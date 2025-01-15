package fr.damnardev.twitch.bot.client.javafx.wrapper;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObservableRaidConfigurationMessage {

	private final SimpleLongProperty id;

	private final SimpleStringProperty name;

	private final SimpleStringProperty message;

	public ObservableRaidConfigurationMessage(Long id, String channel, String message) {
		this.id = new SimpleLongProperty(id);
		this.name = new SimpleStringProperty(channel);
		this.message = new SimpleStringProperty(message);
	}

	public SimpleLongProperty idProperty() {
		return this.id;
	}

	public SimpleStringProperty messageProperty() {
		return this.message;
	}

	public SimpleStringProperty nameProperty() {
		return this.name;
	}

}
