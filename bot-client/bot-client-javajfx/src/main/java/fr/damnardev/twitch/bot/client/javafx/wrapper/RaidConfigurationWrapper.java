package fr.damnardev.twitch.bot.client.javafx.wrapper;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RaidConfigurationWrapper {

	private final SimpleLongProperty id;

	private final SimpleStringProperty name;

	private final SimpleBooleanProperty twitchShoutoutEnabled;

	private final SimpleBooleanProperty wizebotShoutoutEnabled;

	private final SimpleBooleanProperty raidMessageEnabled;

	private final ObservableList<RaidConfigurationMessageWrapper> messages;

	public RaidConfigurationWrapper() {
		this.id = new SimpleLongProperty(0);
		this.name = new SimpleStringProperty("");
		this.twitchShoutoutEnabled = new SimpleBooleanProperty(false);
		this.wizebotShoutoutEnabled = new SimpleBooleanProperty(false);
		this.raidMessageEnabled = new SimpleBooleanProperty(false);
		this.messages = FXCollections.observableArrayList();
	}

	@SuppressWarnings("java:S6204")
	public void update(RaidConfiguration configuration) {
		this.id.set(configuration.channelId());
		this.name.set(configuration.channelName());
		this.twitchShoutoutEnabled.set(configuration.twitchShoutoutEnabled());
		this.wizebotShoutoutEnabled.set(configuration.wizebotShoutoutEnabled());
		this.raidMessageEnabled.set(configuration.raidMessageEnabled());
		this.messages.clear();
		this.messages.addAll(configuration.messages().stream().map((message) -> new RaidConfigurationMessageWrapper(configuration.channelId(), configuration.channelName(), message)).toList());
	}

	public SimpleLongProperty idProperty() {
		return this.id;
	}

	public SimpleStringProperty nameProperty() {
		return this.name;
	}

	public SimpleBooleanProperty twitchShoutoutEnabledProperty() {
		return this.twitchShoutoutEnabled;
	}

	public SimpleBooleanProperty wizebotShoutoutEnabledProperty() {
		return this.wizebotShoutoutEnabled;
	}

	public SimpleBooleanProperty raidMessageEnabledProperty() {
		return this.raidMessageEnabled;
	}

	public ObservableList<RaidConfigurationMessageWrapper> messages() {
		return this.messages;
	}

}
