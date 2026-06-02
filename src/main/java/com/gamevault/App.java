package com.gamevault;

import com.gamevault.model.Game;
import com.gamevault.model.Platform;
import com.gamevault.util.DatabaseCleaner;
import com.gamevault.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;


public class App extends Application {

    @Override
    public void init() throws Exception {
        System.out.println("Vérification et initialisation de la base SQLite...");
        initialiserDonneesSiNecessaire();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties config = loadConfig();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml")
        );
        Parent root = loader.load();

        int width  = Integer.parseInt(config.getProperty("app.window.width",  "1200"));
        int height = Integer.parseInt(config.getProperty("app.window.height", "750"));

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());

        primaryStage.setTitle(config.getProperty("app.name", "GameVault"));
        primaryStage.setMinWidth(820);
        primaryStage.setMinHeight(560);
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

    private void initialiserDonneesSiNecessaire() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Requête JPA pour compter les jeux existants
            Long count = session.createQuery("select count(g) from Game g", Long.class).getSingleResult();

            if (count == 0) {
                System.out.println("Aucune donnée détectée. Initialisation du jeu de données de test...");
                session.beginTransaction();

                // Exemple de jeu de démo respectant tes attributs obligatoires et optionnels
                Game gameTest = new Game();
                gameTest.setTitle("Elden Ring");
                gameTest.setDeveloper("FromSoftware");
                gameTest.setPublisher("Bandai Namco");
                gameTest.setReleaseYear(2022);
                gameTest.setPlatform(Set.of(Platform.PC, Platform.XBOX_ONE, Platform.PLAYSTATION_4));
                gameTest.setPersonalRating(9.1);
                gameTest.setDescription("Un excellent chef-d'œuvre.");
                gameTest.setCoverImagePath("images/game-images/elden_ring.jpg");

                session.persist(gameTest);
                session.getTransaction().commit();
                System.out.println("Jeu de données initialisé avec succès !");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès à SQLite via Hibernate : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
