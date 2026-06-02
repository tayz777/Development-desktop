package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.repository.GameRepositoryImpl;
import com.gamevault.service.GameService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.stream.Collectors;

public class GameListController {

    @FXML private TextField searchField;
    @FXML private TableView<Game> gameTableView;

    @FXML private TableColumn<Game, Void>    colCover;
    @FXML private TableColumn<Game, String>  colTitle;
    @FXML private TableColumn<Game, String>  colPlatform;
    @FXML private TableColumn<Game, Integer> colYear;
    @FXML private TableColumn<Game, Double>  colRating;

    @FXML private Button btnAccueil;
    @FXML private Button btnAjouter;
    @FXML private Button btnCollection;
    @FXML private Button btnAPropos;

    private GameService gameService;
    private ObservableList<Game> allGames;

    @FXML
    public void initialize() {
        gameService = new GameService(new GameRepositoryImpl());

        gameTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        setupColumns();
        styleTable();
        loadGames();

        searchField.textProperty().addListener((obs, o, newVal) -> filterGames(newVal));
    }

    private void setupColumns() {
        // Cover — placeholder coloré
        colCover.setCellFactory(col -> new TableCell<>() {
            private final Region box = new Region();
            {
                box.setPrefSize(48, 64);
                box.setMinSize(48, 64);
                box.setMaxSize(48, 64);
                box.setStyle("-fx-background-color: #2a4080; -fx-background-radius: 6;");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
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
