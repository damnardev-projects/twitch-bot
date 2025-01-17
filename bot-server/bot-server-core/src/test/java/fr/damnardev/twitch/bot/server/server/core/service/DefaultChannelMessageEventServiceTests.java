package fr.damnardev.twitch.bot.server.server.core.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.GlobalCommandType;
import fr.damnardev.twitch.bot.server.model.GlobalCommand;
import fr.damnardev.twitch.bot.server.model.form.ChannelMessageEventForm;
import fr.damnardev.twitch.bot.server.port.primary.DateService;
import fr.damnardev.twitch.bot.server.port.secondary.EventPublisher;
import fr.damnardev.twitch.bot.server.port.secondary.channel.FindChannelRepository;
import fr.damnardev.twitch.bot.server.port.secondary.command.FindChannelCommandRepository;
import fr.damnardev.twitch.bot.server.port.secondary.command.UpdateChannelGlobalCommandRepository;
import fr.damnardev.twitch.bot.server.server.core.service.command.CommandInterpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultChannelMessageEventServiceTests {

	private DefaultChannelMessageEventService channelMessageEventService;

	private DefaultTryService tryService;

	@Mock
	private EventPublisher eventPublisher;

	@Mock
	private FindChannelRepository findChannelRepository;

	@Mock
	private FindChannelCommandRepository findChannelCommandRepository;

	@Mock
	private UpdateChannelGlobalCommandRepository updateChannelGlobalCommandRepository;

	@Mock
	private CommandInterpreter commandInterpreter;

	@Mock
	private DateService dateService;

	@BeforeEach
	void setUp() {
		this.tryService = new DefaultTryService(this.eventPublisher);
		this.tryService = spy(this.tryService);
		given(this.commandInterpreter.getCommandTypeInterpreter()).willReturn(GlobalCommandType.SUGGEST_GAME);
		var commandInterpreters = Collections.singletonList(this.commandInterpreter);
		this.channelMessageEventService = new DefaultChannelMessageEventService(this.tryService, this.findChannelRepository, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, commandInterpreters, this.dateService);
	}

	@Test
	void message_shouldPublishEvent_whenChannelNotFound() {
		// Given
//		var channelName = "channelName";
//		var form = ChannelMessageEventForm.builder().channelName(channelName).build();
//		var captor = ArgumentCaptor.forClass(ErrorEvent.class);
//
//		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.empty());
//
//		// When
//		this.channelMessageEventService.message(form);
//
//		// Then
//		then(this.commandInterpreter).should().getCommandTypeInterpreter();
//		then(this.tryService).should().doTry(any(), eq(form));
//		then(this.findChannelRepository).should().findByName(channelName);
//		then(this.eventPublisher).should().publish(captor.capture());
//		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
//
//		var event = captor.getValue();
//		assertThat(event.getException()).isNotNull().isInstanceOf(BusinessException.class).hasMessage("Channel not found");
	}

	@Test
	void message_shouldDoNothing_whenChannelFoundAndMessageNotStartByToken() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("").build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));

		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

	@Test
	void message_shouldDoNothing_whenChannelFoundAndCommandIsEmpty() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("!foo").build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findChannelCommandRepository.findByChannelAndName(channel, "foo")).willReturn(Optional.empty());


		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findChannelCommandRepository).should().findByChannelAndName(channel, "foo");
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

	@Test
	void message_shouldDoNothing_whenChannelFoundAndCommandNotFound() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var command = GlobalCommand.builder().build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("!foo").build();


		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findChannelCommandRepository.findByChannelAndName(channel, "foo")).willReturn(Optional.of(command));
		given(this.dateService.now()).willReturn(OffsetDateTime.now().minusHours(1));

		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findChannelCommandRepository).should().findByChannelAndName(channel, "foo");
		then(this.dateService).should().now();
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

	@Test
	void message_shouldDoNothing_whenChannelFoundAndCommandFoundButNoValidTime() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var now = OffsetDateTime.now();
		var command = GlobalCommand.builder().lastExecution(now).cooldown(60).build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("!foo").build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findChannelCommandRepository.findByChannelAndName(channel, "foo")).willReturn(Optional.of(command));
		given(this.dateService.now()).willReturn(now);

		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findChannelCommandRepository).should().findByChannelAndName(channel, "foo");
		then(this.dateService).should().now();
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void message_shouldDoNothing_whenChannelFoundAndCommandFoundButNoInterpreter(boolean hasLastExecution) {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var now = OffsetDateTime.now();
		var command = hasLastExecution ? GlobalCommand.builder().lastExecution(now.minusHours(1)).cooldown(60).build() : GlobalCommand.builder().build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("!foo").build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findChannelCommandRepository.findByChannelAndName(channel, "foo")).willReturn(Optional.of(command));
		given(this.dateService.now()).willReturn(now);

		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findChannelCommandRepository).should().findByChannelAndName(channel, "foo");
		then(this.dateService).should().now();
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void message_shouldInvokeInterpreter_whenInterpreterFound(boolean hasLastExecution) {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();

		var now = OffsetDateTime.of(2024, 1, 1, 12, 1, 0, 0, ZoneOffset.of("Z"));
		var previous = OffsetDateTime.of(2024, 1, 1, 12, 0, 0, 0, ZoneOffset.of("Z"));

		var command = hasLastExecution ? GlobalCommand.builder().type(GlobalCommandType.SUGGEST_GAME).lastExecution(previous).cooldown(60).build() : GlobalCommand.builder().type(GlobalCommandType.SUGGEST_GAME).build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("!foo").build();
		var formWithoutToken = ChannelMessageEventForm.builder().channelName(channelName).message("").build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findChannelCommandRepository.findByChannelAndName(channel, "foo")).willReturn(Optional.of(command));
		given(this.dateService.now()).willReturn(now);

		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findChannelCommandRepository).should().findByChannelAndName(channel, "foo");
		then(this.dateService).should().now();
		then(this.commandInterpreter).should().interpret(channel, command, formWithoutToken);
		then(this.updateChannelGlobalCommandRepository).should().update(command.toBuilder().lastExecution(any()).build());
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

	@Test
	void message_shouldInvokeInterpreter_whenParameters() {
		// Given
		var channelName = "channelName";
		var channel = Channel.builder().name(channelName).build();
		var now = OffsetDateTime.of(2024, 1, 1, 12, 1, 0, 0, ZoneOffset.of("Z"));
		var previous = OffsetDateTime.of(2024, 1, 1, 12, 0, 0, 0, ZoneOffset.of("Z"));
		var command = GlobalCommand.builder().type(GlobalCommandType.SUGGEST_GAME).lastExecution(previous).cooldown(60).build();
		var form = ChannelMessageEventForm.builder().channelName(channelName).message("!foo parameterA and parameterB").build();
		var formWithoutToken = ChannelMessageEventForm.builder().channelName(channelName).message("parameterA and parameterB").build();

		given(this.findChannelRepository.findByName(channelName)).willReturn(Optional.of(channel));
		given(this.findChannelCommandRepository.findByChannelAndName(channel, "foo")).willReturn(Optional.of(command));
		given(this.dateService.now()).willReturn(now);

		// When
		this.channelMessageEventService.message(form);

		// Then
		then(this.commandInterpreter).should().getCommandTypeInterpreter();
		then(this.tryService).should().doTry(any(), eq(form));
		then(this.findChannelRepository).should().findByName(channelName);
		then(this.findChannelCommandRepository).should().findByChannelAndName(channel, "foo");
		then(this.dateService).should().now();
		then(this.commandInterpreter).should().interpret(channel, command, formWithoutToken);
		then(this.updateChannelGlobalCommandRepository).should().update(command.toBuilder().lastExecution(now).build());
		verifyNoMoreInteractions(this.tryService, this.findChannelRepository, this.eventPublisher, this.findChannelCommandRepository, this.updateChannelGlobalCommandRepository, this.commandInterpreter, this.dateService);
	}

}
