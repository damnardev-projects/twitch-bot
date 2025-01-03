package fr.damnardev.twitch.bot.server.primary.adapter;

import fr.damnardev.twitch.bot.server.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.channel.CreateChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ChannelControllerTests {

	@InjectMocks
	private ChannelController channelController;

	@Mock
	private FetchAllChannelService fetchAllChannelService;

	@Mock
	private CreateChannelService createChannelService;

	@Test
	void fetchAll_shouldInvokeFetchAllChannelServiceFetchAll_whenCalled() {
		// Given

		// When
		this.channelController.fetchAll();

		// Then
		then(this.fetchAllChannelService).should().fetchAll();
		verifyNoMoreInteractions(this.fetchAllChannelService);
	}

	@Test
	void create_shouldInvokeCreateChannelServiceCreate_whenCalled() {
		// Given
		var form = CreateChannelForm.builder().build();

		// When
		this.createChannelService.create(form);

		// Then
		then(this.createChannelService).should().create(form);
		verifyNoMoreInteractions(this.createChannelService);
	}

}
