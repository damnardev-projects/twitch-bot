package fr.damnardev.twitch.bot.server.server.core.service.command;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.model.Message;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import fr.damnardev.twitch.bot.server.port.secondary.MessageRepository;
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
class NextNoseInterpreterTests {

	private static final ZoneId zoneId = ZoneId.of("Europe/Paris");

	@InjectMocks
	private NextNoseInterpreter nextNoseInterpreter;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private DateService dateService;

	@Test
	void getCommandTypeInterpreter_shouldReturnNextNose() {
		// When
		var commandType = this.nextNoseInterpreter.getCommandTypeInterpreter();

		// Then
		assertThat(commandType).isEqualTo(GlobalCommandType.NEXT_NOSE);
	}

	@Test
	void interpret_shouldSendNextNoseMessage() {
		// Given
		var channelId = 1L;
		var channelName = "channelName";
		var channel = Channel.builder().id(channelId).name(channelName).build();
		var command = GlobalCommand.builder().cooldown(60).build();
		var now = OffsetDateTime.of(2021, 1, 1, 12, 0, 0, 0, ZoneOffset.of("Z"));
		var message = Message.builder().channelId(channelId).channelName(channelName).content("Le prochain nez sera dans 12 minutes 2021-01-01 12:12 [⏰ 60 s]").build();

		given(this.dateService.now(zoneId)).willReturn(now);

		// When
		this.nextNoseInterpreter.interpret(channel, command, null);

		// Then
		then(this.dateService).should().now(zoneId);
		then(this.messageRepository).should().sendMessage(message);
		verifyNoMoreInteractions(this.messageRepository, this.dateService);
	}

	@Test
	void interpret_shouldSendNextNoseMessage_whenMinutesGreaterThanHour() {
		// Given
		var channelId = 1L;
		var channelName = "channelName";
		var channel = Channel.builder().id(channelId).name(channelName).build();
		var command = GlobalCommand.builder().cooldown(60).build();
		var now = OffsetDateTime.of(2021, 1, 1, 12, 13, 0, 0, ZoneOffset.of("Z"));
		var message = Message.builder().channelId(channelId).channelName(channelName).content("Le prochain nez sera dans 60 minutes 2021-01-01 13:13 [⏰ 60 s]").build();

		given(this.dateService.now(zoneId)).willReturn(now);

		// When
		this.nextNoseInterpreter.interpret(channel, command, null);

		// Then
		then(this.dateService).should().now(zoneId);
		then(this.messageRepository).should().sendMessage(message);
		verifyNoMoreInteractions(this.messageRepository, this.dateService);
	}

	@Test
	void interpret_shouldSendNextNoseMessage_whenMinutesEqualsHour() {
		// Given
		var channelId = 1L;
		var channelName = "channelName";
		var channel = Channel.builder().id(channelId).name(channelName).build();
		var command = GlobalCommand.builder().cooldown(60).build();
		var now = OffsetDateTime.of(2021, 1, 1, 12, 12, 0, 0, ZoneOffset.of("Z"));
		var message = Message.builder().channelId(channelId).channelName(channelName).content("Le prochain nez sera dans 0 minutes 2021-01-01 12:12 [⏰ 60 s]").build();

		given(this.dateService.now(zoneId)).willReturn(now);

		// When
		this.nextNoseInterpreter.interpret(channel, command, null);

		// Then
		then(this.dateService).should().now(zoneId);
		then(this.messageRepository).should().sendMessage(message);
		verifyNoMoreInteractions(this.messageRepository, this.dateService);
	}

}
