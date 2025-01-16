package fr.damnardev.twitch.bot.server.database.repository;

import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.database.entity.DbChannelGlobalCommand;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/fr/damnardev/twitch/bot/server/database/repository/db_channel_global_command_repository_tests.sql")
class DbChannelGlobalGlobalCommandRepositoryTests {

	@Autowired
	private DbChannelGlobalCommandRepository dbChannelGlobalCommandRepository;

	@Test
	@Transactional(readOnly = true)
	void findByChannelName_shouldReturnEmptyList_whenChannelNameNotFound() {
		// When
		var result = this.dbChannelGlobalCommandRepository.findByChannelName("name");

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	@Transactional(readOnly = true)
	void findByChannelName_shouldReturnEmptyList_whenChannelNameExistButNoCommand() {
		// When
		var result = this.dbChannelGlobalCommandRepository.findByChannelName("channel_02");

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	@Transactional(readOnly = true)
	void findByChannelName_shouldReturnListChannelConfiguration_whenNameFound() {
		// When
		var result = this.dbChannelGlobalCommandRepository.findByChannelName("channel_01");

		// Then
		var expected = DbChannelGlobalCommand.builder().id(1L).name("!foo")
				.enabled(true)
				.cooldown(60).type(GlobalCommandType.SUGGEST_GAME).build();
		assertThat(result).hasSize(1).first()
				.usingRecursiveComparison().ignoringFields("channel")
				.isEqualTo(expected);
	}

	@Test
	@Transactional(readOnly = true)
	void findByChannelNameAndName_shouldReturnOptionalEmpty_whenChannelNameNotFound() {
		// When
		var result = this.dbChannelGlobalCommandRepository.findByChannelNameAndName("channel_02", "!foo");

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	@Transactional(readOnly = true)
	void findByChannelNameAndName_shouldReturnOptionalEmpty_whenChannelNameFoundButNotName() {
		// When
		var result = this.dbChannelGlobalCommandRepository.findByChannelNameAndName("channel_01", "!bar");

		// Then
		assertThat(result).isEmpty();
	}


	@Test
	@Transactional(readOnly = true)
	void findByChannelNameAndName_shouldReturnOptionalChannelConfiguration_whenChannelNameAndNameFound() {
		// When
		var result = this.dbChannelGlobalCommandRepository.findByChannelNameAndName("channel_01", "!foo");

		// Then
		var expected = DbChannelGlobalCommand.builder().id(1L).name("!foo")
				.enabled(true)
				.cooldown(60)
				.type(GlobalCommandType.SUGGEST_GAME).build();
		assertThat(result).isNotEmpty().get()
				.usingRecursiveComparison().ignoringFields("channel")
				.isEqualTo(expected);
	}

}
