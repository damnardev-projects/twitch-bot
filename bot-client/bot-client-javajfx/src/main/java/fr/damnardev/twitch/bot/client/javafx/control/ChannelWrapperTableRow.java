package fr.damnardev.twitch.bot.client.javafx.control;

import fr.damnardev.twitch.bot.client.javafx.wrapper.ObservableChannel;
import javafx.scene.control.TableRow;

public class ChannelWrapperTableRow extends TableRow<ObservableChannel> {

	private static final String ENABLED_STYLE = "ChannelEnabled";

	private static final String ONLINE_STYLE = "ChannelOnline";

	@Override
	protected void updateItem(ObservableChannel item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			item.getEnabled().addListener((x, y, z) -> this.updateRowColor(this.getItem()));
			item.getOnline().addListener((x, y, z) -> this.updateRowColor(this.getItem()));
		}
		updateRowColor(item);
	}

	private void updateRowColor(ObservableChannel item) {
		getStyleClass().removeAll(ENABLED_STYLE, ONLINE_STYLE);
		if (item == null) {
			setStyle("");
		}
		else if (item.getEnabled().get() && !item.getOnline().get()) {
			getStyleClass().add(ENABLED_STYLE);
		}
		else if (item.getEnabled().get() && item.getOnline().get()) {
			getStyleClass().add(ONLINE_STYLE);
		}
	}

}
