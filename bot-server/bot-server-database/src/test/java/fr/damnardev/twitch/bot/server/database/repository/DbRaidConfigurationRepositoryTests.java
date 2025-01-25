package fr.damnardev.twitch.bot.server.database.repository;

import java.util.List;

import fr.damnardev.twitch.bot.server.database.entity.DbRaidConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/fr/damnardev/twitch/bot/server/database/repository/db_raid_configuration_repository_tests.sql")
class DbRaidConfigurationRepositoryTests {

	@Autowired
	private DbRaidConfigurationRepository dbRaidConfigurationRepository;

	@Test
	@Transactional(readOnly = true)
	void findByChannelIdNotFound() {
		// When
		var result = this.dbRaidConfigurationRepository.findByChannelId(99L);

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	@Transactional(readOnly = true)
	void findByChannelIdExistButNoRaidConfiguration() {
		// When
		var result = this.dbRaidConfigurationRepository.findByChannelId(2L);

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	@Transactional(readOnly = true)
	void findByChannelIdFound() {
		// When
		var result = this.dbRaidConfigurationRepository.findByChannelId(1L);

		// Then
		var expected = DbRaidConfiguration.builder().id(1L).twitchShoutoutEnabled(true).wizebotShoutoutEnabled(true)
				.raidMessageEnabled(true).messages(List.of("raid_message_01")).build();
		assertThat(result).isPresent().get()
				.usingRecursiveComparison().ignoringFields("channel")
				.isEqualTo(expected);
	}

}
