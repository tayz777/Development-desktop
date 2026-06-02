package com.gamevault.util;

import com.gamevault.controller.DetailController;
import com.gamevault.model.Game;
import com.gamevault.repository.GameRepositoryImpl;
import com.gamevault.service.GameService;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class SearchHelper {

    private static final GameService service = new GameService(new GameRepositoryImpl());

    /**
     * Attache l'autocomplétion. Appeler depuis initialize() — gère automatiquement
     * le fait que la scène n'est pas encore disponible.
     */
    public static void attach(TextField field, Runnable onRefresh) {
        // Attendre que la scène + fenêtre soient prêtes
        field.sceneProperty().addListener((obs, old, scene) -> {
            if (scene == null) return;
            scene.windowProperty().addListener((o2, ow, win) -> {
                if (win instanceof Stage s) wire(field, s, onRefresh);
            });
            if (scene.getWindow() instanceof Stage s) wire(field, s, onRefresh);
        });
    }

    private static void wire(TextField field, Stage stage, Runnable onRefresh) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle(
            "-fx-background-color: #0d1f45;" +
            "-fx-border-color: #1e3870; -fx-border-width: 1;"
        );

        field.textProperty().addListener((obs, old, query) -> {
            menu.hide();
            menu.getItems().clear();
            if (query == null || query.isBlank()) return;

            List<Game> results = service.searchByTitle(query.trim());
            if (results.isEmpty()) return;

            for (Game g : results) {
                MenuItem item = new MenuItem(g.getTitle());
                styleItem(item);

                item.setOnAction(e -> {
                    field.setText(g.getTitle());
                    menu.hide();
                    DetailController.open(g, stage, onRefresh);
                });
                menu.getItems().add(item);
            }

            menu.show(field, Side.BOTTOM, 0, 0);
        });

        field.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) menu.hide();
        });
    }

    private static void styleItem(MenuItem item) {
        item.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 6 16 6 16;");
    }
}
