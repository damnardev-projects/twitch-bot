package fr.damnardev.twitch.bot.server.secondary.adapter.raid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.damnardev.twitch.bot.model.RaidConfiguration;
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
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DefaultUpdateRaidConfigurationRepositoryTests {

	@InjectMocks
	private DefaultUpdateRaidConfigurationRepository updateRaidConfigurationRepository;

	@Mock
	private DbRaidConfigurationRepository dbRaidConfigurationRepository;

	@Test
	void update_shouldNotUpdate_whenRaidConfigurationDoesNotExist() {
		// Given
		var raidConfiguration = RaidConfiguration.builder().channelId(1L).messages(List.of("message1", "message2")).build();

		given(this.dbRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.empty());

		// When
		this.updateRaidConfigurationRepository.update(raidConfiguration);

		// Then
		then(this.dbRaidConfigurationRepository).should().findByChannelId(1L);
		verifyNoMoreInteractions(this.dbRaidConfigurationRepository);
	}

	@ParameterizedTest
	@CsvSource({ "true,true,true", "true,true,false", "true,false,true", "true,false,false", "false,true,true", "false,true,false", "false,false,true", "false,false,false" })
	void update_shouldUpdateMessages_whenRaidConfigurationExists(boolean raidMessageEnabled, boolean wizebotShoutoutEnabled, boolean twitchShoutoutEnabled) {
		// Given
		var name = "channelName";
		var messages = List.of("message1", "message2");
		var raidConfiguration = RaidConfiguration.builder().channelId(1L).channelName(name).messages(messages)
				.raidMessageEnabled(raidMessageEnabled).twitchShoutoutEnabled(twitchShoutoutEnabled).wizebotShoutoutEnabled(wizebotShoutoutEnabled).build();
		var dbMessages = spy(new ArrayList<String>());
		var dbRaidConfiguration = DbRaidConfiguration.builder().id(1L)
				.messages(dbMessages).raidMessageEnabled(!raidMessageEnabled)
				.wizebotShoutoutEnabled(!wizebotShoutoutEnabled).twitchShoutoutEnabled(!twitchShoutoutEnabled).build();

		given(this.dbRaidConfigurationRepository.findByChannelId(1L)).willReturn(Optional.of(dbRaidConfiguration));

		// When
		this.updateRaidConfigurationRepository.update(raidConfiguration);

		// Then
		then(this.dbRaidConfigurationRepository).should().findByChannelId(1L);
		then(this.dbRaidConfigurationRepository).should().save(dbRaidConfiguration);
		then(dbMessages).should().clear();
		then(dbMessages).should().addAll(messages);
		verifyNoMoreInteractions(this.dbRaidConfigurationRepository, dbMessages);

		var expected = DbRaidConfiguration.builder().id(1L)
				.messages(dbMessages).raidMessageEnabled(raidMessageEnabled)
				.wizebotShoutoutEnabled(wizebotShoutoutEnabled).twitchShoutoutEnabled(twitchShoutoutEnabled).build();
		assertThat(dbRaidConfiguration)
				.usingRecursiveComparison().ignoringFields("channel")
				.isEqualTo(expected);
	}

}
