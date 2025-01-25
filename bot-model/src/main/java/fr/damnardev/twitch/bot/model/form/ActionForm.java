package fr.damnardev.twitch.bot.model.form;

import lombok.Value;

@Value
public final class ActionForm<T> {

	public static final ActionForm<Void> FETCH_ALL_CHANNEL = new ActionForm<>(ActionKey.FETCH_ALL_CHANNEL);

	public static final ActionForm<String> CREATE_CHANNEL = new ActionForm<>(ActionKey.CREATE_CHANNEL);

	public static final ActionForm<Boolean> UPDATE_CHANNEL_ENABLED = new ActionForm<>(ActionKey.UPDATE_CHANNEL_ENABLED);

	public static final ActionForm<Boolean> UPDATE_CHANNEL_ONLINE = new ActionForm<>(ActionKey.UPDATE_CHANNEL_ONLINE);

	public static final ActionForm<Void> DELETE_CHANNEL = new ActionForm<>(ActionKey.DELETE_CHANNEL);

	public static final ActionForm<Void> FETCH_RAID = new ActionForm<>(ActionKey.FETCH_RAID);

	public static final ActionForm<Void> FETCH_AUTHENTICATED = new ActionForm<>(ActionKey.FETCH_AUTHENTICATED);

	ActionKey key;

	long resourceId;

	T value;

	private ActionForm() {
		this.key = null;
		this.resourceId = 0;
		this.value = null;
	}

	private ActionForm(ActionKey key) {
		this.key = key;
		this.resourceId = 0;
		this.value = null;
	}

	private ActionForm(ActionKey key, long resourceId, T value) {
		this.key = key;
		this.resourceId = resourceId;
		this.value = value;
	}

	public ActionFormBuilder<T> builder() {
		return new ActionFormBuilder<T>().key(this.key);
	}

	public static class ActionFormBuilder<T> {

		private ActionKey key;

		private long resourceId;

		private T value;

		ActionFormBuilder() {
		}

		private ActionFormBuilder<T> key(ActionKey key) {
			this.key = key;
			return this;
		}

		public ActionFormBuilder<T> resourceId(long resourceId) {
			this.resourceId = resourceId;
			return this;
		}

		public ActionFormBuilder<T> value(T value) {
			this.value = value;
			return this;
		}

		public ActionForm<T> build() {
			return new ActionForm<T>(this.key, this.resourceId, this.value);
		}

	}

}
