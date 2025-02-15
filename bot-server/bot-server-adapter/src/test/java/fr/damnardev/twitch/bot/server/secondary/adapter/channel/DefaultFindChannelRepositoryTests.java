package fr.damnardev.twitch.bot.server.secondary.adapter.channel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.server.database.entity.DbChannel;
import fr.damnardev.twitch.bot.server.database.repository.DbChannelRepository;
import fr.damnardev.twitch.bot.server.secondary.mapper.ChannelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultFindChannelRepositoryTests {

	@InjectMocks
	private DefaultFindChannelRepository findChannelRepository;

	@Mock
	private DbChannelRepository dbChannelRepository;

	@Spy
	private ChannelMapper channelMapper;

	@Test
	void findAllEnabled_shouldReturnEmptyList_whenNoEnabledChannels() {
		// Given
		given(this.dbChannelRepository.findAllEnabled()).willReturn(Collections.emptyList());

		// When
		var result = this.findChannelRepository.findAllEnabled();

		// Then
		then(this.dbChannelRepository).should().findAllEnabled();
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		assertThat(result).isEmpty();
	}

	@Test
	void findAllEnabled_shouldReturnListOfChannels_whenEnabledChannelsExist() {
		// Given
		var dbChannel_01 = DbChannel.builder().id(1L).name("channel_01").enabled(true).online(true).build();
		var dbChannel_02 = DbChannel.builder().id(2L).name("channel_02").enabled(false).online(false).build();

		given(this.dbChannelRepository.findAllEnabled()).willReturn(Arrays.asList(dbChannel_01, dbChannel_02));

		// When
		var result = this.findChannelRepository.findAllEnabled();

		// Then
		then(this.dbChannelRepository).should().findAllEnabled();
		then(this.channelMapper).should().toModel(dbChannel_01);
		then(this.channelMapper).should().toModel(dbChannel_02);
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		var channel_01 = Channel.builder().id(1L).name("channel_01").enabled(true).online(true).build();
		var channel_02 = Channel.builder().id(2L).name("channel_02").enabled(false).online(false).build();
		assertThat(result).isNotNull().hasSize(2).contains(channel_01, channel_02);
	}

	@Test
	void findByName_shouldReturnOptionalEmpty_whenNameNotFound() {
		// Given
		var channelName = "channelName";

		given(this.dbChannelRepository.findByName(channelName)).willReturn(Optional.empty());

		// When
		var result = this.findChannelRepository.findByName(channelName);

		// Then
		then(this.dbChannelRepository).should().findByName(channelName);
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		assertThat(result).isEmpty();
	}

	@Test
	void findByName_shouldReturnChannel_whenNameFound() {
		// Given
		var channelName = "channelName";
		var dbChannel = DbChannel.builder().id(1L).name(channelName).enabled(true).online(true).build();

		given(this.dbChannelRepository.findByName(channelName)).willReturn(Optional.of(dbChannel));

		// When
		var result = this.findChannelRepository.findByName(channelName);

		// Then
		then(this.dbChannelRepository).should().findByName(channelName);
		then(this.channelMapper).should().toModel(dbChannel);
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		var expected = Channel.builder().id(1L).name(channelName).enabled(true).online(true).build();
		assertThat(result).isPresent().get().isEqualTo(expected);
	}

	@Test
	void findById_shouldReturnOptionalEmpty_whenIdNotFound() {
		// Given
		var channelId = 1L;

		given(this.dbChannelRepository.findById(channelId)).willReturn(Optional.empty());

		// When
		var result = this.findChannelRepository.findById(channelId);

		// Then
		then(this.dbChannelRepository).should().findById(channelId);
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		assertThat(result).isEmpty();
	}

	@Test
	void findById_shouldReturnChannel_whenIdFound() {
		// Given
		var channelId = 1L;
		var dbChannel = DbChannel.builder().id(channelId).name("foo").enabled(true).online(true).build();

		given(this.dbChannelRepository.findById(channelId)).willReturn(Optional.of(dbChannel));

		// When
		var result = this.findChannelRepository.findById(channelId);

		// Then
		then(this.dbChannelRepository).should().findById(channelId);
		then(this.channelMapper).should().toModel(dbChannel);
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		var expected = Channel.builder().id(channelId).name("foo").enabled(true).online(true).build();
		assertThat(result).isPresent().get().isEqualTo(expected);
	}


	@Test
	void findAll_shouldReturnEmptyList_whenNoChannelsExist() {
		// Given
		given(this.dbChannelRepository.findAll()).willReturn(Collections.emptyList());

		// When
		var result = this.findChannelRepository.findAll();

		// Then
		then(this.dbChannelRepository).should().findAll();
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		assertThat(result).isEmpty();
	}

	@Test
	void findAll_shouldReturnListOfChannels_whenChannelsExist() {
		// Given
		var dbChannel_01 = DbChannel.builder().id(1L).name("channel_01").enabled(true).online(true).build();
		var dbChannel_02 = DbChannel.builder().id(2L).name("channel_02").enabled(false).online(false).build();

		given(this.dbChannelRepository.findAll()).willReturn(Arrays.asList(dbChannel_01, dbChannel_02));

		// When
		var result = this.findChannelRepository.findAll();

		// Then
		then(this.dbChannelRepository).should().findAll();
		then(this.channelMapper).should().toModel(dbChannel_01);
		then(this.channelMapper).should().toModel(dbChannel_02);
		verifyNoMoreInteractions(this.dbChannelRepository, this.channelMapper);

		var channel_01 = Channel.builder().id(1L).name("channel_01").enabled(true).online(true).build();
		var channel_02 = Channel.builder().id(2L).name("channel_02").enabled(false).online(false).build();
		assertThat(result).isNotNull().hasSize(2).contains(channel_01, channel_02);
	}

}
