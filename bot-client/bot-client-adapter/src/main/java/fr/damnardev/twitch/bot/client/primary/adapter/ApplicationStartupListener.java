package fr.damnardev.twitch.bot.client.primary.adapter;

import fr.damnardev.twitch.bot.client.handler.StompClientBotHandler;
import fr.damnardev.twitch.bot.client.port.primary.StartupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApplicationStartupListener implements ApplicationRunner {

	private final ThreadPoolTaskExecutor executor;

	private final StartupService startupService;

	private final ConfigurableApplicationContext springContext;

	private final StompClientBotHandler stompClientBotHandler;

	@Override
	public void run(ApplicationArguments args) {
		this.executor.execute(this::doInternal);
	}

	private void doInternal() {
		log.info("Starting client bot");
		this.startupService.run(this.springContext, ConfigurableApplicationContext::close, (t) -> t::getBean);
		this.stompClientBotHandler.connect();
		log.info("Client bot started");
	}

}
