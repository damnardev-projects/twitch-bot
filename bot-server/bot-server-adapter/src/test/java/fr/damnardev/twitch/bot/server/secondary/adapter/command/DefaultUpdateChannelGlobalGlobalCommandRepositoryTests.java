package fr.damnardev.twitch.bot.server.secondary.adapter.command;

import java.time.OffsetDateTime;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.database.entity.DbChannel;
import fr.damnardev.twitch.bot.server.database.entity.DbChannelGlobalCommand;
import fr.damnardev.twitch.bot.server.database.repository.DbChannelGlobalCommandRepository;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultUpdateChannelGlobalGlobalCommandRepositoryTests {

	@InjectMocks
	private DefaultUpdateChannelGlobalCommandRepository updateChannelCommandRepository;

	@Mock
	private DbChannelGlobalCommandRepository dbChannelGlobalCommandRepository;

	@Test
	void update_shouldNotUpdate_whenCommandDoesNotExist() {
		// Given
		var channelName = "channelName";
		var name = "!foo";
		var command = GlobalCommand.builder().channelName(channelName).name(name).build();

		given(this.dbChannelGlobalCommandRepository.findByChannelNameAndName(channelName, name)).willReturn(Optional.empty());

		// When
		this.updateChannelCommandRepository.update(command);

		// Then
		then(this.dbChannelGlobalCommandRepository).should().findByChannelNameAndName(channelName, name);
		verifyNoMoreInteractions(this.dbChannelGlobalCommandRepository);
	}

	@Test
	void update_shouldUpdate_whenCommandExists() {
		// Given
		var channelName = "channelName";
		var name = "!foo";
		var command = GlobalCommand.builder().channelName(channelName).name(name).type(GlobalCommandType.SAINT).enabled(true).cooldown(60).lastExecution(OffsetDateTime.now()).build();
		var dbChannelCommand = DbChannelGlobalCommand.builder().id(1L).channel(DbChannel.builder().name(channelName).build()).name(name).enabled(false).cooldown(30).lastExecution(null).build();

		given(this.dbChannelGlobalCommandRepository.findByChannelNameAndName(channelName, name)).willReturn(Optional.of(dbChannelCommand));

		// When
		this.updateChannelCommandRepository.update(command);

		// Then
		then(this.dbChannelGlobalCommandRepository).should().findByChannelNameAndName(channelName, name);
		then(this.dbChannelGlobalCommandRepository).should().save(dbChannelCommand);
		verifyNoMoreInteractions(this.dbChannelGlobalCommandRepository);

		var expected = DbChannelGlobalCommand.builder().id(1L).channel(DbChannel.builder().name(channelName).build()).name(name).type(GlobalCommandType.SAINT).enabled(true).cooldown(60).lastExecution(command.lastExecution()).build();
		assertThat(dbChannelCommand).usingRecursiveComparison().ignoringFields("channel").isEqualTo(expected);
	}

}
