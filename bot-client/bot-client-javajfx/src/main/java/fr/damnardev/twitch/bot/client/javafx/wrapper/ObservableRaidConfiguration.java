package fr.damnardev.twitch.bot.client.javafx.wrapper;

import com.sun.javafx.binding.ExpressionHelper;
import fr.damnardev.twitch.bot.model.RaidConfiguration;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class ObservableRaidConfiguration implements ObservableValue<RaidConfiguration> {

	private final SimpleLongProperty id;

	private final SimpleStringProperty name;

	private final SimpleBooleanProperty twitchShoutoutEnabled;

	private final SimpleBooleanProperty wizebotShoutoutEnabled;

	private final SimpleBooleanProperty raidMessageEnabled;

	private final ObservableList<String> messages;

	private ExpressionHelper<RaidConfiguration> helper;

	public ObservableRaidConfiguration() {
		this.id = new SimpleLongProperty(0);
		this.name = new SimpleStringProperty("");
		this.twitchShoutoutEnabled = new SimpleBooleanProperty(false);
		this.wizebotShoutoutEnabled = new SimpleBooleanProperty(false);
		this.raidMessageEnabled = new SimpleBooleanProperty(false);
		this.messages = FXCollections.observableArrayList();

		this.twitchShoutoutEnabled.addListener((observable, oldValue, newValue) -> ExpressionHelper.fireValueChangedEvent(this.helper));
		this.wizebotShoutoutEnabled.addListener((observable, oldValue, newValue) -> ExpressionHelper.fireValueChangedEvent(this.helper));
		this.raidMessageEnabled.addListener((observable, oldValue, newValue) -> ExpressionHelper.fireValueChangedEvent(this.helper));
		this.messages.addListener((ListChangeListener<? super String>) (c) -> ExpressionHelper.fireValueChangedEvent(this.helper));
	}

	@SuppressWarnings("java:S6204")
	public void update(RaidConfiguration configuration) {
		this.id.set(configuration.channelId());
		this.name.set(configuration.channelName());
		this.twitchShoutoutEnabled.set(configuration.twitchShoutoutEnabled());
		this.wizebotShoutoutEnabled.set(configuration.wizebotShoutoutEnabled());
		this.raidMessageEnabled.set(configuration.raidMessageEnabled());
		this.messages.clear();
		this.messages.addAll(configuration.messages());
	}

	@Override
	public void addListener(ChangeListener<? super RaidConfiguration> listener) {
		this.helper = ExpressionHelper.addListener(this.helper, this, listener);
	}

	@Override
	public void removeListener(ChangeListener<? super RaidConfiguration> listener) {
		this.helper = ExpressionHelper.removeListener(this.helper, listener);
	}

	@Override
	public RaidConfiguration getValue() {
		return RaidConfiguration.builder().channelId(this.id.get()).channelName(this.name.get()).twitchShoutoutEnabled(this.twitchShoutoutEnabled.get()).wizebotShoutoutEnabled(this.wizebotShoutoutEnabled.get()).raidMessageEnabled(this.raidMessageEnabled.get()).messages(this.messages.stream().toList()).build();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		this.helper = ExpressionHelper.addListener(this.helper, this, listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		this.helper = ExpressionHelper.removeListener(this.helper, listener);
	}

}
