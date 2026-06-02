package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.repository.GameRepositoryImpl;
import com.gamevault.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.io.FileInputStream;

public class DetailController {

    @FXML private ImageView coverImage;
    @FXML private Label     labelTitle;
    @FXML private Label     labelDeveloper;
    @FXML private Label     labelPublisher;
    @FXML private Label     labelYear;
    @FXML private Label     labelDescription;
    @FXML private Button    btnFavorite;

    private Game game;
    private Stage stage;
    private Runnable onRefresh; // callback pour rafraîchir la page parente

    // ── API publique ──────────────────────────────────────────────────────

    public void setGame(Game g) {
        this.game = g;
        labelTitle.setText(g.getTitle());
        labelDeveloper.setText(g.getDeveloper() != null ? g.getDeveloper() : "");
        labelPublisher.setText(g.getPublisher() != null ? g.getPublisher() : "");
        labelYear.setText(g.getReleaseYear() != null ? String.valueOf(g.getReleaseYear()) : "");
        labelDescription.setText(g.getDescription() != null ? g.getDescription() : "");
        boolean fav = Boolean.TRUE.equals(g.getIsFavorite());
        btnFavorite.setText(fav ? "★" : "☆");
        btnFavorite.setStyle(fav ? "-fx-text-fill: #f0c040;" : "-fx-text-fill: rgba(255,255,255,0.6);");
        loadCover(g);
    }

    public void setStage(Stage s)          { this.stage = s; }
    public void setOnRefresh(Runnable r)   { this.onRefresh = r; }

    // ── Actions ───────────────────────────────────────────────────────────

    @FXML private void onClose() { stage.close(); }

    @FXML
    private void onToggleFavorite() {
        boolean newVal = !Boolean.TRUE.equals(game.getIsFavorite());
        game.setIsFavorite(newVal);
        new GameService(new GameRepositoryImpl()).updateGame(game);
        btnFavorite.setText(newVal ? "★" : "☆");
        btnFavorite.setStyle(newVal ? "-fx-text-fill: #f0c040;" : "-fx-text-fill: rgba(255,255,255,0.6);");
        if (onRefresh != null) onRefresh.run();
    }

    @FXML
    private void onDelete() {
        new GameService(new GameRepositoryImpl()).deleteGame(game.getId());
        stage.close();
        if (onRefresh != null) onRefresh.run();
    }

    @FXML
    private void onModify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addgame.fxml"));
            Parent root = loader.load();
            AddGameController ctrl = loader.getController();
            ctrl.setGame(game); // pré-remplir

            Stage owner = stage;
            stage.close();

            // Remplacer la scène principale
            Stage mainStage = (Stage) owner.getOwner();
            if (mainStage == null) return;
            Scene scene = new Scene(root, mainStage.getScene().getWidth(), mainStage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
            mainStage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ── Utilitaire image ──────────────────────────────────────────────────

    private void loadCover(Game g) {
        if (coverImage == null || g.getCoverImagePath() == null) return;
        String path = g.getCoverImagePath();
        try (InputStream is = getClass().getResourceAsStream("/" + path)) {
            if (is != null) { Image img = new Image(is); if (!img.isError()) { coverImage.setImage(img); return; } }
        } catch (Exception ignored) {}
        try {
            java.io.File classesDir = new java.io.File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            java.io.File root = classesDir.getParentFile().getParentFile();
            for (String r : new String[]{root + "/src/main/resources/", root + "/target/classes/"}) {
                java.io.File f = new java.io.File(r + path);
                if (f.exists()) {
                    try (InputStream is2 = new FileInputStream(f)) {
                        Image img = new Image(is2);
                        if (!img.isError()) { coverImage.setImage(img); return; }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    // ── Factory statique ─────────────────────────────────────────────────

    public static void open(Game game, Stage owner, Runnable onRefresh) {
        try {
            FXMLLoader loader = new FXMLLoader(
                DetailController.class.getResource("/fxml/detail.fxml")
            );
            Parent root = loader.load();
            DetailController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initOwner(owner);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.setScene(new Scene(root));
            dialog.getScene().getStylesheets().add(
                DetailController.class.getResource("/css/style.css").toExternalForm()
            );
            dialog.getScene().getStylesheets().add(
                DetailController.class.getResource("/css/main.css").toExternalForm()
            );
            dialog.getScene().setFill(javafx.scene.paint.Color.TRANSPARENT);
            dialog.initStyle(StageStyle.TRANSPARENT);

            ctrl.setGame(game);
            ctrl.setStage(dialog);
            ctrl.setOnRefresh(onRefresh);

            dialog.showAndWait();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
