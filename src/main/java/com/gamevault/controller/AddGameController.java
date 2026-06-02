package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.model.Platform;
import com.gamevault.repository.GameRepositoryImpl;
import com.gamevault.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

public class AddGameController {

    @FXML private ImageView logoImage;
    @FXML private ImageView avatarImage;
    @FXML private ImageView avatarTopBar;

    @FXML private Button btnAccueil;
    @FXML private Button btnAjouter;
    @FXML private Button btnCollection;
    @FXML private Button btnAPropos;

    @FXML private TextField  fieldTitle;
    @FXML private TextArea   fieldDescription;
    @FXML private TextField  fieldDeveloper;
    @FXML private TextField  fieldPublisher;
    @FXML private TextField  fieldYear;
    @FXML private TextField  fieldRating;
    @FXML private TextField  fieldImagePath;
    @FXML private ComboBox<Platform> comboPlatform;
    @FXML private Label      statusLabel;

    private File selectedImageFile;

    private final GameService gameService = new GameService(new GameRepositoryImpl());

    @FXML
    public void initialize() {
        loadImage("/images/logo-dragon.png",      logoImage);
        loadImage("/images/logo-tete-dragon.png", avatarTopBar);
        loadImage("/images/logo-tete-dragon.png", avatarImage);

        comboPlatform.getItems().addAll(Platform.values());
        comboPlatform.getSelectionModel().selectFirst();
    }

    @FXML
    private void onAjouter_jeu() {
        statusLabel.setStyle("-fx-font-size: 13px; -fx-padding: 0 0 12 0;");

        String title = fieldTitle.getText().trim();
        if (title.isEmpty()) {
            statusLabel.setText("Le titre est obligatoire.");
            statusLabel.setStyle("-fx-text-fill: #ff6060; -fx-font-size: 13px;");
            return;
        }

        Platform platform = comboPlatform.getValue();
        if (platform == null) {
            statusLabel.setText("Sélectionne une plateforme.");
            statusLabel.setStyle("-fx-text-fill: #ff6060; -fx-font-size: 13px;");
            return;
        }

        Game game = new Game();
        game.setTitle(title);
        game.setDescription(fieldDescription.getText().trim());
        game.setDeveloper(fieldDeveloper.getText().trim());
        game.setPublisher(fieldPublisher.getText().trim());
        game.setPlatform(Set.of(platform));

        String yearStr = fieldYear.getText().trim();
        if (!yearStr.isEmpty()) {
            try { game.setReleaseYear(Integer.parseInt(yearStr)); }
            catch (NumberFormatException e) {
                statusLabel.setText("Année invalide.");
                statusLabel.setStyle("-fx-text-fill: #ff6060; -fx-font-size: 13px;");
                return;
            }
        }

        String ratingStr = fieldRating.getText().trim();
        if (!ratingStr.isEmpty()) {
            try {
                double r = Double.parseDouble(ratingStr);
                if (r < 0 || r > 10) throw new NumberFormatException();
                game.setPersonalRating(r);
            } catch (NumberFormatException e) {
                statusLabel.setText("Note invalide (0-10).");
                statusLabel.setStyle("-fx-text-fill: #ff6060; -fx-font-size: 13px;");
                return;
            }
        }

        // Copie image dans resources/images/game-images/
        if (selectedImageFile != null) {
            try {
                URL resUrl = getClass().getResource("/images/game-images/");
                Path destDir;
                if (resUrl != null) {
                    destDir = Paths.get(resUrl.toURI());
                } else {
                    // Fallback : dossier src/main/resources/images/game-images
                    destDir = Paths.get("src/main/resources/images/game-images");
                }
                Files.createDirectories(destDir);
                String filename = selectedImageFile.getName();
                Path dest = destDir.resolve(filename);
                Files.copy(selectedImageFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                game.setCoverImagePath("images/game-images/" + filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        gameService.addGame(game);
        statusLabel.setText("Jeu \"" + title + "\" ajouté avec succès !");
        statusLabel.setStyle("-fx-text-fill: #60d080; -fx-font-size: 13px;");
        clearForm();
    }

    @FXML
    private void onBrowseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Sélectionner une image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp")
        );
        File file = chooser.showOpenDialog(fieldImagePath.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            fieldImagePath.setText(file.getName());
        }
    }

    private void clearForm() {
        fieldTitle.clear();
        fieldDescription.clear();
        fieldDeveloper.clear();
        fieldPublisher.clear();
        fieldYear.clear();
        fieldRating.clear();
        fieldImagePath.clear();
        selectedImageFile = null;
        comboPlatform.getSelectionModel().selectFirst();
    }

    // ── Navigation ────────────────────────────────────────────────────────

    @FXML private void onAccueil()    { navigateTo("/fxml/main.fxml", false); }
    @FXML private void onAjouter()    { /* déjà ici */ }
    @FXML private void onAPropos()    { /* TODO */ }
    @FXML private void onCollection() {
        GameListController.filterFavorites = false;
        navigateTo("/fxml/gamelists.fxml", true);
    }

    private void navigateTo(String fxml, boolean withGamelists) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) btnAccueil.getScene().getWindow();
            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
            if (withGamelists)
                scene.getStylesheets().add(getClass().getResource("/css/gamelists.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImage(String path, ImageView target) {
        if (target == null) return;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) target.setImage(new Image(is));
        } catch (Exception ignored) {}
    }
}
