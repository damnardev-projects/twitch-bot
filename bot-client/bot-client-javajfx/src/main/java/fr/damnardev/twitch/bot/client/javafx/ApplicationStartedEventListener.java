package fr.damnardev.twitch.bot.client.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import fr.damnardev.twitch.bot.client.port.primary.StartupService;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.exception.FatalException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@DomainService
public class ApplicationStartedEventListener implements StartupService {

	private final ClientRepository clientRepository;

	@Override
	public <T> void run(T instance, Consumer<T> closeMethod, Function<T, Function<Class<?>, Object>> getBeanMethod) {
		Platform.startup(() -> {
			try {
				createWindow(instance, closeMethod, getBeanMethod);
				Platform.setImplicitExit(true);
				this.clientRepository.connect();
			}
			catch (IOException ex) {
				throw new FatalException(ex);
			}
		});
	}

	private <T> void createWindow(T instance, Consumer<T> closeMethod, Function<T, Function<Class<?>, Object>> getBeanMethod) throws IOException {
		var stage = new Stage();
		var scene = loadScene(instance, getBeanMethod);
		URL resource = getClass().getResource("/fr/damnardev/twitch/bot/client/primary/javafx/view/main.css");
		scene.getStylesheets().add(resource.toExternalForm());
		stage.setScene(scene);
		setupStage(stage, instance, closeMethod);
	}

	private <T> void setupStage(Stage stage, T instance, Consumer<T> closeMethod) {
		stage.setTitle("Twitch Bot");
		stage.setMinWidth(1280);
		stage.setMinHeight(720);
		stage.setAlwaysOnTop(false);
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fr/damnardev/twitch/bot/client/primary/javafx/icon.png"))));
		stage.setOnCloseRequest((x) -> closeMethod.accept(instance));
		stage.show();
	}

	private <T> Scene loadScene(T instance, Function<T, Function<Class<?>, Object>> getBeanMethod) throws IOException {
		var fxmlLoader = new FXMLLoader(getClass().getResource("/fr/damnardev/twitch/bot/client/primary/javafx/view/main.fxml"));
		fxmlLoader.setControllerFactory((x) -> getBeanMethod.apply(instance).apply(x));
		return new Scene(fxmlLoader.load(), 600, 600);
	}

}
