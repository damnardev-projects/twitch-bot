package fr.damnardev.twitch.bot.model.form;

import lombok.Value;

@Value
public final class ActionKey {

	public static final ActionKey FETCH_ALL_CHANNEL = new ActionKey(ResourceType.CHANNEL, ActionType.FETCH_ALL, "");

	public static final ActionKey CREATE_CHANNEL = new ActionKey(ResourceType.CHANNEL, ActionType.CREATE, "");

	public static final ActionKey UPDATE_CHANNEL_ENABLED = new ActionKey(ResourceType.CHANNEL, ActionType.UPDATE, "enabled");

	public static final ActionKey UPDATE_CHANNEL_ONLINE = new ActionKey(ResourceType.CHANNEL, ActionType.UPDATE, "online");

	public static final ActionKey DELETE_CHANNEL = new ActionKey(ResourceType.CHANNEL, ActionType.DELETE, "");

	public static final ActionKey FETCH_AUTHENTICATED = new ActionKey(ResourceType.AUTHENTICATED, ActionType.FETCH, "");

	ResourceType resource;

	ActionType action;

	String key;

	private ActionKey() {
		this.resource = null;
		this.action = null;
		this.key = null;
	}

	private ActionKey(ResourceType resource, ActionType action, String key) {
		this.resource = resource;
		this.action = action;
		this.key = key;
	}

}
