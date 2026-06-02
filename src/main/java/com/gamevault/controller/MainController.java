package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.repository.GameRepositoryImpl;
import com.gamevault.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    // ── Sidebar ───────────────────────────────────────────────────────────
    @FXML
    private ImageView logoImage;
    @FXML private ImageView avatarSidebar;
    @FXML private Button    btnAccueil;
    @FXML private Button    btnAjouter;
    @FXML private Button    btnCollection;
    @FXML private Button    btnAPropos;

    // ── Topbar ────────────────────────────────────────────────────────────
    @FXML private ImageView avatarTopBar;
    @FXML private TextField searchField;
    @FXML private Label     usernameLabel;

    // ── Contenu principal (responsive) ──────────────────────────────────
    @FXML private ScrollPane mainScrollPane;
    @FXML private VBox       contentVBox;

    // ── Favoris ───────────────────────────────────────────────────────────
    @FXML private VBox favoritesContent;

    // ── Overlay détail ────────────────────────────────────────────────────
    @FXML private StackPane overlay;
    @FXML private Button    btnFavorite;

    // ── Featured card ─────────────────────────────────────────────────────
    @FXML private Label featuredTitle;
    @FXML private Label detailTitle;
    @FXML private Label detailDeveloper;
    @FXML private Label detailPublisher;
    @FXML private Label detailYear;
    @FXML private Label detailDescription;

    // ── Game cards ────────────────────────────────────────────────────────
    @FXML private Label     gameCard1Title;
    @FXML private Label     gameCard2Title;
    @FXML private Label     gameCard3Title;
    @FXML private StackPane featuredCard;
    @FXML private StackPane gameCard1;
    @FXML private StackPane gameCard2;
    @FXML private StackPane gameCard3;
    @FXML private ImageView featuredImage;
    @FXML private ImageView cardImage1;
    @FXML private ImageView cardImage2;
    @FXML private ImageView cardImage3;

    private Game currentDetailGame;
    private List<Game> cardGames = new java.util.ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImage("/images/logo-dragon.png",      logoImage);
        loadImage("/images/logo-tete-dragon.png", avatarTopBar);
        loadImage("/images/logo-tete-dragon.png", avatarSidebar);
        setActiveNav(btnAccueil);
        if (usernameLabel != null)
            usernameLabel.setText(com.gamevault.util.UserSession.getUsername());
        mainScrollPane.viewportBoundsProperty().addListener((obs, o, bounds) ->
                contentVBox.setMinHeight(bounds.getHeight())
        );
        loadFeaturedGame();
        loadGameCards();
        loadFavoritePanel();
    }

    // ── Jeu aléatoire ─────────────────────────────────────────────────────

    private void loadFeaturedGame() {
        GameService service = new GameService(new GameRepositoryImpl());
        List<Game> games = service.getAllGames();
        if (games.isEmpty()) return;

        currentDetailGame = games.get(new Random().nextInt(games.size()));
        showInOverlay(currentDetailGame);
        featuredTitle.setText(currentDetailGame.getTitle());
        loadCoverInto(currentDetailGame, featuredImage);
        bindAndClip(featuredImage, featuredCard, 14);
    }

    private void loadGameCards() {
        GameService service = new GameService(new GameRepositoryImpl());
        List<Game> all = service.getAllGames();
        if (all.isEmpty()) return;

        Random rnd = new Random();
        List<Label>     labels  = List.of(gameCard1Title, gameCard2Title, gameCard3Title);
        List<ImageView> ivs     = List.of(cardImage1, cardImage2, cardImage3);
        List<StackPane> panes   = List.of(gameCard1, gameCard2, gameCard3);
        cardGames.clear();

        // Pool sans featured sauf si pas assez de jeux
        java.util.List<Game> pool = new java.util.ArrayList<>(all);
        if (pool.size() > 1 && currentDetailGame != null) pool.remove(currentDetailGame);

        for (int i = 0; i < labels.size(); i++) {
            // Réutiliser le pool cycliquement si moins de 3 jeux
            Game g = pool.get(rnd.nextInt(pool.size()));
            cardGames.add(g);
            labels.get(i).setText(g.getTitle());
            loadCoverInto(g, ivs.get(i));
            bindAndClip(ivs.get(i), panes.get(i), 14);
        }
    }

    private void loadFavoritePanel() {
        GameService service = new GameService(new GameRepositoryImpl());
        List<Game> favs = service.getAllGames().stream()
                .filter(g -> Boolean.TRUE.equals(g.getIsFavorite()))
                .collect(Collectors.toList());

        favoritesContent.getChildren().clear();
        if (favs.isEmpty()) {
            Label empty = new Label("Aucun jeu en favori");
            empty.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 12px;");
            favoritesContent.getChildren().add(empty);
        } else {
            Game fav = favs.get(new Random().nextInt(favs.size()));
            Label lbl = new Label("♥  " + fav.getTitle());
            lbl.setStyle("-fx-text-fill: #ff6080; -fx-font-size: 13px;");
            favoritesContent.getChildren().add(lbl);
        }
    }

    // ── Handlers cartes jeux ──────────────────────────────────────────────

    @FXML private void onGameCard1Click() { openDetailForCard(0); }
    @FXML private void onGameCard2Click() { openDetailForCard(1); }
    @FXML private void onGameCard3Click() { openDetailForCard(2); }

    private void openDetailForCard(int index) {
        if (index >= cardGames.size()) return;
        currentDetailGame = cardGames.get(index);
        showInOverlay(currentDetailGame);
        overlay.setVisible(true);
    }

    private void showInOverlay(Game g) {
        detailTitle.setText(g.getTitle());
        detailDeveloper.setText(g.getDeveloper() != null ? g.getDeveloper() : "");
        detailPublisher.setText(g.getPublisher() != null ? g.getPublisher() : "");
        detailYear.setText(g.getReleaseYear() != null ? String.valueOf(g.getReleaseYear()) : "");
        detailDescription.setText(g.getDescription() != null ? g.getDescription() : "");
        boolean fav = Boolean.TRUE.equals(g.getIsFavorite());
        btnFavorite.setText(fav ? "★" : "☆");
        btnFavorite.setStyle(fav ? "-fx-text-fill: #f0c040;" : "-fx-text-fill: rgba(255,255,255,0.6);");
    }

    @FXML
    private void onFavoritesClick() {
        GameListController.filterFavorites = true;
        onCollection();
    }

    private void bindAndClip(ImageView iv, StackPane pane, double radius) {
        iv.setPreserveRatio(true);

        // Clip arrondi permanent
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.widthProperty().bind(pane.widthProperty());
        clip.heightProperty().bind(pane.heightProperty());
        clip.setArcWidth(radius * 2);
        clip.setArcHeight(radius * 2);
        pane.setClip(clip);

        // Taille fixée UNE SEULE FOIS quand la carte a sa largeur finale
        javafx.beans.value.ChangeListener<Number>[] holder = new javafx.beans.value.ChangeListener[1];
        holder[0] = (obs, old, w) -> {
            if (w.doubleValue() > 0) {
                iv.setFitWidth(w.doubleValue());
                pane.widthProperty().removeListener(holder[0]);
            }
        };
        if (pane.getWidth() > 0) {
            iv.setFitWidth(pane.getWidth());
        } else {
            pane.widthProperty().addListener(holder[0]);
        }
    }

    private void loadCoverInto(Game g, ImageView iv) {
        if (iv == null || g.getCoverImagePath() == null) return;
        String path = g.getCoverImagePath();
        // Essai 1 : classpath (target/classes)
        try (InputStream is = getClass().getResourceAsStream("/" + path)) {
            if (is != null) { Image img = new Image(is); if (!img.isError()) { iv.setImage(img); return; } }
        } catch (Exception ignored) {}
        try {
            java.io.File classesDir = new java.io.File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            java.io.File projectRoot = classesDir.getParentFile().getParentFile();
            for (String root : new String[]{
                    projectRoot + "/src/main/resources/",
                    projectRoot + "/target/classes/"}) {
                java.io.File f = new java.io.File(root + path);
                if (f.exists()) {
                    try (InputStream is2 = new java.io.FileInputStream(f)) {
                        Image img = new Image(is2);
                        if (!img.isError()) { iv.setImage(img); return; }
                    }
                }
            }
        } catch (Exception ignored) {}
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
    @FXML private void onAPropos()    { setActiveNav(btnAPropos); }

    @FXML
    private void onAjouter() {
        setActiveNav(btnAjouter);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addgame.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCollection() {
        setActiveNav(btnCollection);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamelists.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCollection.getScene().getWindow();
            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/gamelists.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        if (currentDetailGame != null) showInOverlay(currentDetailGame);
        overlay.setVisible(true);
    }

    @FXML
    private void onCloseDetail() {
        overlay.setVisible(false);
    }

    // ── Favori étoile ────────────────────────────────────────────────────

    @FXML
    private void onToggleFavorite() {
        if (currentDetailGame == null) return;
        boolean newVal = !Boolean.TRUE.equals(currentDetailGame.getIsFavorite());
        currentDetailGame.setIsFavorite(newVal);
        new GameService(new GameRepositoryImpl()).updateGame(currentDetailGame);
        btnFavorite.setText(newVal ? "★" : "☆");
        btnFavorite.setStyle(newVal
                ? "-fx-text-fill: #f0c040;"
                : "-fx-text-fill: rgba(255,255,255,0.6);");
        loadFavoritePanel();
    }

    // ── Supprimer ─────────────────────────────────────────────────────────

    @FXML
    private void onDelete() {
        if (currentDetailGame == null) return;
        new GameService(new GameRepositoryImpl()).deleteGame(currentDetailGame.getId());
        overlay.setVisible(false);
        currentDetailGame = null;
        loadFeaturedGame();
        loadGameCards();
        loadFavoritePanel();
    }

    @FXML
    private void onModify() {
        // TODO
    }
}
