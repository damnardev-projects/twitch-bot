package org.damnardev.twitch.bot.primary.javafx.adapter;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damnardev.twitch.bot.domain.exception.FatalException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApplicationStartupListener implements ApplicationRunner {

    private final ConfigurableApplicationContext springContext;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting GUI");
        Platform.startup(() -> {
            try {
                createWindow();
            } catch (IOException e) {
                throw new FatalException(e);
            }
        });
        Platform.setImplicitExit(true);
        log.info("GUI started");
    }

    private void createWindow() throws IOException {
        var stage = new Stage();
        var fxmlLoader = new FXMLLoader(ApplicationStartupListener.class.getResource("bot-twitch-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        var scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Twich Bot");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(event -> {
            springContext.close();
            Platform.exit();
        });
        stage.show();
    }

}
