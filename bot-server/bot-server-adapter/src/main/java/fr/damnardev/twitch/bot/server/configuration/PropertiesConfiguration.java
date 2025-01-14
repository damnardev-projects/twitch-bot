package fr.damnardev.twitch.bot.server.configuration;

import fr.damnardev.twitch.bot.server.secondary.property.TwitchOAuthProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class PropertiesConfiguration {

	@Bean(name = "twitch4JProperties")
	@ConfigurationProperties(prefix = "twitch.oauth")
	public TwitchOAuthProperties twitch4JProperties() {
		return new TwitchOAuthProperties();
	}

}
