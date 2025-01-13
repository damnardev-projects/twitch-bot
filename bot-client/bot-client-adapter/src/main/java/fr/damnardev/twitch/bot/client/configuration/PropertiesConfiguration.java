package fr.damnardev.twitch.bot.client.configuration;

import fr.damnardev.twitch.bot.client.property.BotProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class PropertiesConfiguration {

	@Bean(name = "botProperties")
	@ConfigurationProperties(prefix = "bot.server")
	public BotProperties botProperties() {
		return new BotProperties();
	}

}
