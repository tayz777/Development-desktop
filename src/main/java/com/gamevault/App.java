package com.gamevault;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties config = loadConfig();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/main.fxml")
        );
        Parent root = loader.load();

        int width  = Integer.parseInt(config.getProperty("app.window.width",  "1200"));
        int height = Integer.parseInt(config.getProperty("app.window.height", "750"));

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
        );

        primaryStage.setTitle(config.getProperty("app.name", "GameVault"));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/config.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            // Utilisation des valeurs par défaut si le fichier est absent
        }
        return props;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
