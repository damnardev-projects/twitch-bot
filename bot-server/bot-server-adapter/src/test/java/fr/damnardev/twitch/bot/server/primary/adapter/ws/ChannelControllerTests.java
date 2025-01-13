package fr.damnardev.twitch.bot.server.primary.adapter.ws;

import fr.damnardev.twitch.bot.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.model.form.UpdateChannelForm;
import fr.damnardev.twitch.bot.server.port.primary.channel.CreateChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.DeleteChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.server.port.primary.channel.UpdateChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

	@Mock
	private UpdateChannelService updateChannelService;

	@Mock
	private DeleteChannelService deleteChannelService;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Test
	void fetchAll_shouldInvokeFetchAllChannelServiceFetchAll_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);

		// When
		this.channelController.fetchAll();

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.fetchAllChannelService).should().fetchAll();
		verifyNoMoreInteractions(this.executor, this.fetchAllChannelService);
	}

	@Test
	void create_shouldInvokeCreateChannelServiceCreate_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);
		var form = CreateChannelForm.builder().build();

		// When
		this.channelController.create(form);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.createChannelService).should().create(form);
		verifyNoMoreInteractions(this.executor, this.createChannelService);
	}

	@Test
	void update_shouldInvokeUpdateChannelServiceUpdate_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);
		var form = UpdateChannelForm.builder().build();

		// When
		this.channelController.update(form);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.updateChannelService).should().update(form);
		verifyNoMoreInteractions(this.executor, this.updateChannelService);
	}

	@Test
	void delete_shouldInvokeDeleteChannelServiceDelete_whenCalled() {
		// Given
		var captor = ArgumentCaptor.forClass(Runnable.class);
		var form = DeleteChannelForm.builder().build();

		// When
		this.channelController.delete(form);

		// Then
		then(this.executor).should().execute(captor.capture());
		captor.getValue().run();
		then(this.deleteChannelService).should().delete(form);
		verifyNoMoreInteractions(this.executor, this.deleteChannelService);
	}

}
