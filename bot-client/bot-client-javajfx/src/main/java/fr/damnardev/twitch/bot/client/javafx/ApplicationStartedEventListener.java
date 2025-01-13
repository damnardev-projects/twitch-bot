package fr.damnardev.twitch.bot.client.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import fr.damnardev.twitch.bot.client.port.primary.StartupService;
import fr.damnardev.twitch.bot.client.port.secondary.ClientRepository;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.exception.FatalException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@DomainService
@Slf4j
public class ApplicationStartedEventListener implements StartupService {

	private final ClientRepository clientRepository;

	private final Map<String, Node> nodeByName = new HashMap<>();

	private Function<Class<?>, Object> controllerFactory;

	private Runnable closeMethod;

	@Override
	public void run(Runnable closeMethod, Function<Class<?>, Object> controllerFactory) {
		Platform.startup(() -> {
			try {
				this.controllerFactory = controllerFactory;
				this.closeMethod = closeMethod;
				createWindow();
				Platform.setImplicitExit(true);
				loadAllNode();
				this.clientRepository.connect();
			}
			catch (IOException ex) {
				throw new FatalException(ex);
			}
		});
	}

	private void loadAllNode() throws IOException {
		var fxmlLoader = new FXMLLoader(getClass().getResource("/fr/damnardev/twitch/bot/client/primary/javafx/view/dashboardPane.fxml"));
		fxmlLoader.setControllerFactory((x) -> this.controllerFactory.apply(x));
		this.nodeByName.put("dashboard", fxmlLoader.load());
	}

	private <T> void createWindow() throws IOException {
		var stage = new Stage();
		var scene = loadScene();
		URL resource = getClass().getResource("/fr/damnardev/twitch/bot/client/primary/javafx/view/main.css");
		scene.getStylesheets().add(resource.toExternalForm());
		stage.setScene(scene);
		setupStage(stage);
	}

	private <T> void setupStage(Stage stage) {
		stage.setTitle("Twitch Bot");
		stage.setMinWidth(1280);
		stage.setMinHeight(720);
		stage.setAlwaysOnTop(false);
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fr/damnardev/twitch/bot/client/primary/javafx/icon.png"))));
		stage.setOnCloseRequest((x) -> this.closeMethod.run());
		stage.show();
	}

	private <T> Scene loadScene() throws IOException {
		var fxmlLoader = new FXMLLoader(getClass().getResource("/fr/damnardev/twitch/bot/client/primary/javafx/view/main.fxml"));
		fxmlLoader.setControllerFactory((x) -> this.controllerFactory.apply(x));
		return new Scene(fxmlLoader.load(), 600, 600);
	}

	public Node get(String name) {
		return this.nodeByName.get(name);
	}

}
