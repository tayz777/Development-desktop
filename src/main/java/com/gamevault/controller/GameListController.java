package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.repository.GameRepositoryImpl;
import com.gamevault.service.GameService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class GameListController {

    @FXML private TextField  searchField;
    @FXML private TableView<Game> gameTableView;

    @FXML private TableColumn<Game, Void>    colCover;
    @FXML private TableColumn<Game, String>  colTitle;
    @FXML private TableColumn<Game, String>  colPlatform;
    @FXML private TableColumn<Game, Integer> colYear;
    @FXML private TableColumn<Game, Double>  colRating;
    @FXML private TableColumn<Game, Void>    colFavorite;

    @FXML private Button    btnAccueil;
    @FXML private Button    btnAjouter;
    @FXML private Button    btnCollection;
    @FXML private Button    btnAPropos;
    @FXML private ImageView logoImage;
    @FXML private ImageView avatarImage;
    @FXML private ImageView avatarTopBar;
    @FXML private Label     usernameLabel;

    private GameService gameService;
    private ObservableList<Game> allGames;

    public static boolean filterFavorites = false;

    @FXML
    public void initialize() {
        loadImage("/images/logo-dragon.png",      logoImage);
        loadImage("/images/logo-tete-dragon.png", avatarTopBar);
        loadImage("/images/logo-tete-dragon.png", avatarImage);
        if (usernameLabel != null)
            usernameLabel.setText(com.gamevault.util.UserSession.getUsername());

        gameService = new GameService(new GameRepositoryImpl());
        gameTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        setupColumns();
        styleTable();
        loadGames();

        searchField.textProperty().addListener((obs, o, newVal) -> filterGames(newVal));
        com.gamevault.util.SearchHelper.attach(searchField, this::loadGames);

        gameTableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Game> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1 && !row.isEmpty()) {
                    Game g = row.getItem();
                    javafx.stage.Stage stage = (javafx.stage.Stage) gameTableView.getScene().getWindow();
                    DetailController.open(g, stage, () -> {
                        loadGames();
                    });
                }
            });
            return row;
        });

        if (filterFavorites) {
            filterFavorites = false;
            ObservableList<Game> favs = allGames.stream()
                    .filter(g -> Boolean.TRUE.equals(g.getIsFavorite()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            gameTableView.setItems(favs);
        }
    }

    private javafx.scene.image.Image loadCoverImage(Game g) {
        String path = g.getCoverImagePath();
        if (path == null || path.isBlank()) return null;
        try (InputStream is = getClass().getResourceAsStream("/" + path)) {
            if (is != null) return new javafx.scene.image.Image(is);
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
                        return new javafx.scene.image.Image(is2);
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void loadImage(String path, ImageView target) {
        if (target == null) return;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) target.setImage(new Image(is));
        } catch (Exception ignored) {}
    }

    @FXML private void onAccueil() {
        navigateTo("/fxml/main.fxml", false);
    }

    @FXML private void onAjouter() {
        navigateTo("/fxml/addgame.fxml", false);
    }

    @FXML private void onCollection() { /* déjà ici */ }

    @FXML private void onAPropos() {
        showAboutPopup();
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

    private void showAboutPopup() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.NONE
        );
        alert.setTitle("À Propos");
        alert.setHeaderText("DRAGO Games");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(12);
        content.setPadding(new javafx.geometry.Insets(16));
        content.getChildren().addAll(
            styledLabel("Gestionnaire de collection de jeux vidéo", true),
            styledLabel("Version 1.0", false),
            styledLabel("Développé avec JavaFX 21 + Hibernate + SQLite", false),
            styledLabel("© 2026 DragoGames", false)
        );

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);
        alert.getDialogPane().setStyle(
            "-fx-background-color: #0d1f45; -fx-border-color: #1e3870; -fx-border-width: 1;"
        );
        alert.getDialogPane().lookup(".header-panel").setStyle(
            "-fx-background-color: #0d1f45;"
        );
        // Style header text
        javafx.scene.control.Label header = (javafx.scene.control.Label)
            alert.getDialogPane().lookup(".header-panel .label");
        if (header != null) header.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        alert.initOwner(btnAccueil.getScene().getWindow());
        alert.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    private javafx.scene.control.Label styledLabel(String text, boolean bold) {
        javafx.scene.control.Label lbl = new javafx.scene.control.Label(text);
        lbl.setStyle("-fx-text-fill: " + (bold ? "white" : "#a0b4d6") +
                     "; -fx-font-size: " + (bold ? "13" : "12") + "px;" +
                     (bold ? " -fx-font-weight: bold;" : ""));
        return lbl;
    }

    private void setupColumns() {
        // Cover — ImageView comme les logos
        colCover.setCellFactory(col -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            private final Region placeholder = new Region();
            {
                iv.setFitWidth(48); iv.setFitHeight(64);
                iv.setPreserveRatio(true); iv.setSmooth(true);
                placeholder.setPrefSize(48, 64);
                placeholder.setMinSize(48, 64);
                placeholder.setMaxSize(48, 64);
                placeholder.setStyle("-fx-background-color: #2a4080; -fx-background-radius: 6;");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Game g = getTableView().getItems().get(getIndex());
                Image img = loadCoverImage(g);
                if (img != null) { iv.setImage(img); setGraphic(iv); }
                else setGraphic(placeholder);
                setAlignment(Pos.CENTER);
            }
        });

        // Favori — étoile cliquable
        colFavorite.setCellFactory(col -> new TableCell<>() {
            private final Button star = new Button();
            {
                star.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 0;");
                star.setOnAction(e -> {
                    Game g = getTableView().getItems().get(getIndex());
                    boolean newVal = !Boolean.TRUE.equals(g.getIsFavorite());
                    g.setIsFavorite(newVal);
                    gameService.updateGame(g);
                    star.setText(newVal ? "★" : "☆");
                    star.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 0; -fx-text-fill: " + (newVal ? "#f0c040;" : "rgba(255,255,255,0.4);"));
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Game g = getTableView().getItems().get(getIndex());
                boolean fav = Boolean.TRUE.equals(g.getIsFavorite());
                star.setText(fav ? "★" : "☆");
                star.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 0; -fx-text-fill: " + (fav ? "#f0c040;" : "rgba(255,255,255,0.4);"));
                setGraphic(star);
                setAlignment(Pos.CENTER);
            }
        });

        // Titre
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colTitle.setCellFactory(col -> styledCell(true));

        // Plateforme — jointure des enums
        colPlatform.setCellValueFactory(data -> {
            Game g = data.getValue();
            String txt = g.getPlatform() == null ? "—"
                    : g.getPlatform().stream().map(p -> p.getLabel()).collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(txt);
        });
        colPlatform.setCellFactory(col -> styledCell(false));

        // Année
        colYear.setCellValueFactory(data -> {
            Integer y = data.getValue().getReleaseYear();
            return new javafx.beans.property.SimpleObjectProperty<>(y);
        });
        colYear.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
                setTextFill(Color.WHITE);
                setFont(Font.font("System", 13));
                setAlignment(Pos.CENTER_LEFT);
                setStyle("-fx-background-color: transparent;");
            }
        });

        // Note
        colRating.setCellValueFactory(data -> {
            Double r = data.getValue().getPersonalRating();
            return new javafx.beans.property.SimpleObjectProperty<>(r);
        });
        colRating.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
                setTextFill(Color.WHITE);
                setFont(Font.font("System", 13));
                setAlignment(Pos.CENTER_LEFT);
                setStyle("-fx-background-color: transparent;");
            }
        });
    }

    private <T> TableCell<Game, T> styledCell(boolean bold) {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setTextFill(Color.WHITE);
                setFont(bold ? Font.font("System", FontWeight.BOLD, 14) : Font.font("System", 13));
                setAlignment(Pos.CENTER_LEFT);
                setStyle("-fx-background-color: transparent;");
            }
        };
    }

    private void styleTable() {
        gameTableView.setFixedCellSize(96);
        gameTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    private void loadGames() {
        List<Game> games = gameService.getAllGames();
        allGames = FXCollections.observableArrayList(games);
        gameTableView.setItems(allGames);
    }

    private void filterGames(String query) {
        if (query == null || query.isBlank()) {
            gameTableView.setItems(allGames);
            return;
        }
        String lower = query.toLowerCase();
        ObservableList<Game> filtered = allGames.stream()
                .filter(g -> g.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        gameTableView.setItems(filtered);
    }
}
