package fr.damnardev.twitch.bot.server.server.core.service.command;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.model.Message;
import fr.damnardev.twitch.bot.server.model.SuggestGame;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;
import fr.damnardev.twitch.bot.server.port.secondary.SuggestGameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class SuggestGameInterpreterTests {

	@InjectMocks
	private SuggestGameInterpreter suggestGameInterpreter;

	@Mock
	private SuggestGameRepository suggestGameRepository;

	@Mock
	private MessageRepository messageRepository;

	@Test
	void getCommandTypeInterpreter_shouldReturnCommandTypeSuggestGame() {
		// Given
		// When
		var result = this.suggestGameInterpreter.getCommandTypeInterpreter();

		// Then
		verifyNoMoreInteractions(this.messageRepository, this.suggestGameRepository);

		assertThat(result).isEqualTo(GlobalCommandType.SUGGEST_GAME);
	}

	@Test
	void interpret_shouldDoNothing_whenIfGameIsNull() {
		// Given
		var channel = Channel.builder().id(1L).name("channel_name").build();
		var command = GlobalCommand.builder().name("game").build();
		var form = ChannelMessageEventForm.builder().sender("foo").message(null).build();
		var message = Message.builder().channelId(1L).channelName("channel_name").content("/me usage: !game nom_du_jeu").build();

		// When
		var response = assertThatThrownBy(() -> this.suggestGameInterpreter.interpret(channel, command, form));

		// Then
		then(this.messageRepository).should().sendMessage(message);
		verifyNoMoreInteractions(this.messageRepository, this.suggestGameRepository);

		response.isInstanceOf(RuntimeException.class).hasMessage("No game suggested");
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " ", "  " })
	void interpret_shouldDoNothing_whenIfGameIsEmpty(String source) {
		// Given
		var channel = Channel.builder().id(1L).name("channel_name").build();
		var command = GlobalCommand.builder().name("game").build();
		var form = ChannelMessageEventForm.builder().sender("foo").message(null).build();
		var message = Message.builder().channelId(1L).channelName("channel_name").content("/me usage: !game nom_du_jeu").build();

		// When
		var response = assertThatThrownBy(() -> this.suggestGameInterpreter.interpret(channel, command, form));

		// Then
		then(this.messageRepository).should().sendMessage(message);
		verifyNoMoreInteractions(this.messageRepository, this.suggestGameRepository);

		response.isInstanceOf(RuntimeException.class).hasMessage("No game suggested");
	}

	@Test
	void interpret_shouldDoNothing_whenSuggestGameReturnFalse() {
		// Given
		var channel = Channel.builder().id(1L).name("channel_name").build();
		var suggestGame = SuggestGame.builder().viewer("foo").game("bar").build();
		var command = GlobalCommand.builder().build();
		var form = ChannelMessageEventForm.builder().sender("foo").message("bar").build();

		given(this.suggestGameRepository.suggest(channel, suggestGame)).willReturn(false);

		// When
		this.suggestGameInterpreter.interpret(channel, command, form);

		// Then
		then(this.suggestGameRepository).should().suggest(channel, suggestGame);
		verifyNoMoreInteractions(this.messageRepository, this.suggestGameRepository);
	}

	@Test
	void interpret_shouldSendMessage_whenSuggestGameReturnTrue() {
		// Given
		var channel = Channel.builder().id(1L).name("channel_name").build();
		var suggestGame = SuggestGame.builder().viewer("foo").game("bar").build();
		var command = GlobalCommand.builder().build();
		var form = ChannelMessageEventForm.builder().sender("foo").message("bar").build();
		var message = Message.builder().channelId(1L).channelName("channel_name").content("Merci pour la suggestion du jeu [⏰ 0 s]").build();

		given(this.suggestGameRepository.suggest(channel, suggestGame)).willReturn(true);

		// When
		this.suggestGameInterpreter.interpret(channel, command, form);

		// Then
		then(this.suggestGameRepository).should().suggest(channel, suggestGame);
		then(this.messageRepository).should().sendMessage(message);
		verifyNoMoreInteractions(this.messageRepository, this.suggestGameRepository);
	}

}
