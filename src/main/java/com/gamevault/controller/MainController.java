package com.gamevault.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // ── Sidebar ───────────────────────────────────────────────────────────
    @FXML private ImageView logoImage;
    @FXML private ImageView avatarSidebar;
    @FXML private Button    btnAccueil;
    @FXML private Button    btnAjouter;
    @FXML private Button    btnCollection;
    @FXML private Button    btnAPropos;

    // ── Topbar ────────────────────────────────────────────────────────────
    @FXML private ImageView avatarTopBar;
    @FXML private TextField searchField;

    // ── Contenu principal (responsive) ──────────────────────────────────
    @FXML private ScrollPane mainScrollPane;
    @FXML private VBox       contentVBox;

    // ── Favoris ───────────────────────────────────────────────────────────
    @FXML private VBox favoritesContent;

    // ── Overlay détail ────────────────────────────────────────────────────
    @FXML private StackPane overlay;
    @FXML private Button    btnFavorite;

    private boolean isFavorite = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
<<<<<<< HEAD
        loadImage("/images/logo-dragon.png",      logoImage);
        loadImage("/images/logo-tete-dragon.png", avatarTopBar);
        loadImage("/images/logo-tete-dragon.png", avatarSidebar);
        setActiveNav(btnAccueil);
        // Le contenu remplit toujours la hauteur visible de la fenêtre
        mainScrollPane.viewportBoundsProperty().addListener((obs, o, bounds) ->
                contentVBox.setMinHeight(bounds.getHeight())
        );
    }

    // ── Chargement d'image ───────────────────────────────────────────────

    private void loadImage(String path, ImageView target) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                target.setImage(new Image(is));
            }
        } catch (Exception e) {
            // Image absente : l'ImageView reste vide, l'app continue
        }
    }

    // ── Navigation ───────────────────────────────────────────────────────

    @FXML private void onAccueil()    { setActiveNav(btnAccueil); }
    @FXML private void onAjouter()    { setActiveNav(btnAjouter); }
    @FXML private void onCollection() { setActiveNav(btnCollection); }
    @FXML private void onAPropos()    { setActiveNav(btnAPropos); }

    private void setActiveNav(Button active) {
        List<Button> all = List.of(btnAccueil, btnAjouter, btnCollection, btnAPropos);
        for (Button btn : all) {
            btn.getStyleClass().remove("nav-btn-active");
        }
        active.getStyleClass().add("nav-btn-active");
    }

    // ── Overlay : ouverture / fermeture ──────────────────────────────────

    @FXML
    private void onFeaturedCardClick() {
        overlay.setVisible(true);
    }

    @FXML
    private void onCloseDetail() {
        overlay.setVisible(false);
    }

    // ── Favoris ──────────────────────────────────────────────────────────

    @FXML
    private void onToggleFavorite() {
        isFavorite = !isFavorite;
        if (isFavorite) {
            btnFavorite.setText("♥");
            btnFavorite.getStyleClass().add("detail-heart-btn-active");
            Label item = new Label("♥  Minecraft");
            item.getStyleClass().add("favorite-item");
            favoritesContent.getChildren().add(item);
        } else {
            btnFavorite.setText("♡");
            btnFavorite.getStyleClass().remove("detail-heart-btn-active");
            favoritesContent.getChildren().clear();
        }
    }

    // ── Modifier / Supprimer (à compléter) ───────────────────────────────

    @FXML
    private void onModify() {
        // TODO : ouvrir le formulaire d'édition
    }

    @FXML
    private void onDelete() {
        overlay.setVisible(false);
=======
>>>>>>> 088baca100b1af8a71c667b6952a28860dfd91fb
    }
}
