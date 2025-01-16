package fr.damnardev.twitch.bot.server.secondary.adapter.command;

import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
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
class DefaultFindChannelGlobalCommandRepositoryTests {

	@InjectMocks
	private DefaultFindChannelCommandRepository findChannelCommandRepository;

	@Mock
	private DbChannelGlobalCommandRepository dbChannelGlobalCommandRepository;

	@Test
	void findByChannelAndName_shouldReturnEmptyMap_whenNotFound() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var commandName = "commandName";

		given(this.dbChannelGlobalCommandRepository.findByChannelNameAndName(channelName, commandName)).willReturn(Optional.empty());

		// When
		var result = this.findChannelCommandRepository.findByChannelAndName(channel, commandName);

		// Then
		then(this.dbChannelGlobalCommandRepository).should().findByChannelNameAndName(channelName, commandName);
		verifyNoMoreInteractions(this.dbChannelGlobalCommandRepository);

		assertThat(result).isEmpty();
	}

	@Test
	void findByChannelAndName_shouldReturnEmptyMap_whenFound() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var commandName = "commandName";

		var message = "message";
		var dbCommand = DbChannelGlobalCommand.builder().id(1L).channel(DbChannel.builder().name(channelName).build()).name("!foo").enabled(true).type(GlobalCommandType.SAINT).cooldown(60).lastExecution(null).build();

		given(this.dbChannelGlobalCommandRepository.findByChannelNameAndName(channelName, commandName)).willReturn(Optional.of(dbCommand));

		// When
		var result = this.findChannelCommandRepository.findByChannelAndName(channel, commandName);

		// Then
		then(this.dbChannelGlobalCommandRepository).should().findByChannelNameAndName(channelName, commandName);
		verifyNoMoreInteractions(this.dbChannelGlobalCommandRepository);

		var expected = GlobalCommand.builder().channelId(1L).channelName(channelName).name("!foo").type(GlobalCommandType.SAINT).enabled(true).cooldown(60).lastExecution(null).build();
		assertThat(result).isNotEmpty().get().isEqualTo(expected);
	}

}
