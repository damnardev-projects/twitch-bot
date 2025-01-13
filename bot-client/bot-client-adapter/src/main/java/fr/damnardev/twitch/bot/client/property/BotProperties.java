package fr.damnardev.twitch.bot.client.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class BotProperties {

	private String url;

	private String username;

	private String password;

}
