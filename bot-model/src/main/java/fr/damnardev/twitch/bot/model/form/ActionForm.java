package fr.damnardev.twitch.bot.model.form;

import lombok.Value;

@Value
public final class ActionForm<T> {

	public static final ActionForm<Boolean> UPDATE_RAID_ONLINE = new ActionForm<>(ActionKey.UPDATE_RAID_ONLINE);

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

	public ResourceActionBuilder<T> builder() {
		return new ResourceActionBuilder<T>().key(this.key);
	}

	public static class ResourceActionBuilder<T> {

		private ActionKey key;

		private long resourceId;

		private T value;

		ResourceActionBuilder() {
		}

		private ResourceActionBuilder<T> key(ActionKey key) {
			this.key = key;
			return this;
		}

		public ResourceActionBuilder<T> resourceId(long resourceId) {
			this.resourceId = resourceId;
			return this;
		}

		public ResourceActionBuilder<T> value(T value) {
			this.value = value;
			return this;
		}

		public ActionForm<T> build() {
			return new ActionForm<T>(this.key, this.resourceId, this.value);
		}

	}

}
