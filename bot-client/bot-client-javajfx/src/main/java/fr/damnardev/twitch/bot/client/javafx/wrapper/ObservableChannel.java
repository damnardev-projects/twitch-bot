package fr.damnardev.twitch.bot.client.javafx.wrapper;

import com.sun.javafx.binding.ExpressionHelper;
import fr.damnardev.twitch.bot.model.Channel;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.Data;

@Data
public class ObservableChannel implements ObservableValue<Channel> {

	private final SimpleLongProperty id;

	private final SimpleStringProperty name;

	private final SimpleBooleanProperty enabled;

	private final SimpleBooleanProperty online;

	private ExpressionHelper<Channel> helper;

	public ObservableChannel(Channel channel) {
		this.id = new SimpleLongProperty(channel.id());
		this.name = new SimpleStringProperty(channel.name());
		this.enabled = new SimpleBooleanProperty(channel.enabled());
		this.online = new SimpleBooleanProperty(channel.online());

		this.enabled.addListener((observable, oldValue, newValue) -> ExpressionHelper.fireValueChangedEvent(this.helper));
	}

	@Override
	public void addListener(ChangeListener<? super Channel> listener) {
		this.helper = ExpressionHelper.addListener(this.helper, this, listener);
	}

	@Override
	public void removeListener(ChangeListener<? super Channel> listener) {
		this.helper = ExpressionHelper.removeListener(this.helper, listener);
	}

	@Override
	public Channel getValue() {
		return Channel.builder().id(this.id.get()).name(this.name.get()).enabled(this.enabled.get()).online(this.online.get()).build();
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
