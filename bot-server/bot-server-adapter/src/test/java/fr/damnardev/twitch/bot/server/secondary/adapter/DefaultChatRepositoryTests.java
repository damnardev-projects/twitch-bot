package fr.damnardev.twitch.bot.server.secondary.adapter;

import java.util.Arrays;
import java.util.Collections;

import com.github.twitch4j.TwitchClientHelper;
import com.github.twitch4j.chat.TwitchChat;
import fr.damnardev.twitch.bot.model.Channel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultChatRepositoryTests {

	@InjectMocks
	private DefaultChatRepository chatRepository;

	@Mock
	private TwitchChat twitchChat;

	@Mock
	private TwitchClientHelper twitchClientHelper;

	@Test
	void reconnect_shouldInvokeReconnect_whenCalled() {
		// Given

		// When
		this.chatRepository.reconnect();

		// Then
		then(this.twitchChat).should().reconnect();
		verifyNoMoreInteractions(this.twitchChat, this.twitchClientHelper);
	}

	@Test
	void joinAll_shouldDoNothing_whenListIsEmpty() {
		// When
		this.chatRepository.joinAll(Collections.emptyList());

		// Then
		verifyNoMoreInteractions(this.twitchChat, this.twitchClientHelper);
	}

	@Test
	void joinAll_shouldJoinChannelsAndListenEvent_whenCalled() {
		// Given
		var channel_01 = Channel.builder().id(1L).name("channel_01").enabled(true).build();
		var channel_02 = Channel.builder().id(2L).name("channel_02").enabled(true).build();

		given(this.twitchClientHelper.enableStreamEventListener(channel_01.id().toString(), channel_01.name())).willReturn(false);
		given(this.twitchClientHelper.enableStreamEventListener(channel_02.id().toString(), channel_02.name())).willReturn(false);

		// When
		this.chatRepository.joinAll(Arrays.asList(channel_01, channel_02));

		// Then
		then(this.twitchChat).should().joinChannel(channel_01.name());
		then(this.twitchChat).should().joinChannel(channel_02.name());
		then(this.twitchClientHelper).should().enableStreamEventListener(channel_01.id().toString(), channel_01.name());
		then(this.twitchClientHelper).should().enableStreamEventListener(channel_02.id().toString(), channel_02.name());
		verifyNoMoreInteractions(this.twitchChat, this.twitchClientHelper);
	}

	@Test
	void join_shouldJoinChannelsAndListenEvent_whenCalled() {
		// Given
		var channel = Channel.builder().id(1L).name("channel").enabled(true).build();

		given(this.twitchClientHelper.enableStreamEventListener(channel.id().toString(), channel.name())).willReturn(false);

		// When
		this.chatRepository.join(channel);

		// Then
		then(this.twitchChat).should().joinChannel(channel.name());
		then(this.twitchClientHelper).should().enableStreamEventListener(channel.id().toString(), channel.name());
		verifyNoMoreInteractions(this.twitchChat, this.twitchClientHelper);
	}


	@Test
	void leave_shouldLeaveChannelAndDisableEvent_whenCalled() {
		// Given
		var channel = Channel.builder().id(1L).name("channel").enabled(true).build();

		given(this.twitchChat.leaveChannel(channel.name())).willReturn(false);

		// When
		this.chatRepository.leave(channel);

		// Then
		then(this.twitchChat).should().leaveChannel(channel.name());
		then(this.twitchClientHelper).should().disableStreamEventListener(channel.name());
		verifyNoMoreInteractions(this.twitchChat, this.twitchClientHelper);
	}

}
