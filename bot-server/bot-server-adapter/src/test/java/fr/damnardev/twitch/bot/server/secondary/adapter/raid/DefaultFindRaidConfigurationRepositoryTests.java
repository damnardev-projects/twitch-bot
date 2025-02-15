package fr.damnardev.twitch.bot.server.secondary.adapter.raid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
import fr.damnardev.twitch.bot.server.database.entity.DbChannel;
import fr.damnardev.twitch.bot.server.database.entity.DbRaidConfiguration;
import fr.damnardev.twitch.bot.server.database.repository.DbRaidConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultFindRaidConfigurationRepositoryTests {

	@InjectMocks
	private DefaultFindRaidConfigurationRepository findRaidConfigurationRepository;

	@Mock
	private DbRaidConfigurationRepository dbRaidConfigurationRepository;

	@Test
	void findByChannel_Id_shouldReturnOptionalEmpty_whenNotFound() {
		// Given
		given(this.dbRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.empty());

		// When
		var result = this.findRaidConfigurationRepository.findByChannelId(1L);

		// Then
		then(this.dbRaidConfigurationRepository).should().findByChannelId(1L);
		verifyNoMoreInteractions(this.dbRaidConfigurationRepository);

		assertThat(result).isEmpty();
	}

	@ParameterizedTest
	@CsvSource({ "true,true,true", "true,true,false", "true,false,true", "true,false,false", "false,true,true", "false,true,false", "false,false,true", "false,false,false" })
	void findByChannel_shouldReturnChannel_Id_whenNameFound(boolean raidMessageEnabled, boolean wizebotShoutoutEnabled, boolean twitchShoutoutEnabled) {
		// Given
		var channelName = "channelName";
		var message = "message";
		var dbRaidConfiguration = DbRaidConfiguration.builder().id(1L)
				.channel(DbChannel.builder().name(channelName).build())
				.raidMessageEnabled(raidMessageEnabled).twitchShoutoutEnabled(twitchShoutoutEnabled).wizebotShoutoutEnabled(wizebotShoutoutEnabled)
				.messages(Collections.singletonList(message)).build();

		given(this.dbRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.of(dbRaidConfiguration));

		// When
		var result = this.findRaidConfigurationRepository.findByChannelId(1L);

		// Then
		then(this.dbRaidConfigurationRepository).should().findByChannelId(1L);
		verifyNoMoreInteractions(this.dbRaidConfigurationRepository);

		var expected = RaidConfiguration.builder().channelId(1L).channelName(channelName)
				.raidMessageEnabled(raidMessageEnabled).twitchShoutoutEnabled(twitchShoutoutEnabled).wizebotShoutoutEnabled(wizebotShoutoutEnabled)
				.messages(Collections.singletonList(message)).build();
		assertThat(result).isPresent().get().isEqualTo(expected);
	}

	@Test
	void findAll_shouldReturnEmptyList_whenNoChannelsExist() {
		// Given
		given(this.dbRaidConfigurationRepository.findAll()).willReturn(Collections.emptyList());

		// When
		var result = this.findRaidConfigurationRepository.findAll();

		// Then
		then(this.dbRaidConfigurationRepository).should().findAll();
		verifyNoMoreInteractions(this.dbRaidConfigurationRepository);

		assertThat(result).isEmpty();
	}

	@ParameterizedTest
	@CsvSource({ "true,true,true", "true,true,false", "true,false,true", "true,false,false", "false,true,true", "false,true,false", "false,false,true", "false,false,false" })
	void findAll_shouldReturnListOfChannels_whenChannelsExist(boolean raidMessageEnabled, boolean wizebotShoutoutEnabled, boolean twitchShoutoutEnabled) {
		// Given
		var channelName = "channelName";
		var dbChannel = DbChannel.builder().name(channelName).build();
		var dbRaidConfiguration = DbRaidConfiguration.builder().id(1L).channel(dbChannel)
				.raidMessageEnabled(raidMessageEnabled).wizebotShoutoutEnabled(wizebotShoutoutEnabled)
				.twitchShoutoutEnabled(twitchShoutoutEnabled).build();

		given(this.dbRaidConfigurationRepository.findAll()).willReturn(Collections.singletonList(dbRaidConfiguration));

		// When
		var result = this.findRaidConfigurationRepository.findAll();

		// findRaidConfigurationRepository
		then(this.dbRaidConfigurationRepository).should().findAll();
		verifyNoMoreInteractions(this.dbRaidConfigurationRepository);

		var raidConfiguration = RaidConfiguration.builder().channelId(1L).channelName(channelName).raidMessageEnabled(raidMessageEnabled)
				.wizebotShoutoutEnabled(wizebotShoutoutEnabled).twitchShoutoutEnabled(twitchShoutoutEnabled).messages(new ArrayList<>()).build();
		assertThat(result).isNotNull().hasSize(1).contains(raidConfiguration);
	}

}
