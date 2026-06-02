package com.gamevault.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField username_textarea;

    @FXML
    private Button validate_button;

    @FXML
    private void onLoginClick(ActionEvent event) {
        // Optionnel : Tu peux ajouter une petite vérification sur le texte saisi
        String username = username_textarea.getText().trim();
        if (username.isEmpty()) {
            Alert usernameAlert =new Alert(Alert.AlertType.INFORMATION);
            usernameAlert.setTitle("Erreur");
            usernameAlert.setHeaderText(null);
            usernameAlert.setContentText("Veuillez rentrer un nom d'utilisateur");
            usernameAlert.showAndWait();
            return;
        }

        com.gamevault.util.UserSession.setUsername(username);

        try {
            // 1. Charger la page principale (main.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent mainRoot = loader.load();

            // 2. Récupérer le Stage (la fenêtre) actuel à partir du bouton cliqué
            Stage stage = (Stage) validate_button.getScene().getWindow();

            // 3. Créer la nouvelle scène avec le menu principal
            Scene mainScene = new Scene(mainRoot, stage.getScene().getWidth(), stage.getScene().getHeight());

            mainScene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            mainScene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
            mainScene.getStylesheets().add(getClass().getResource("/css/gamelists.css").toExternalForm());

            // 4. Changer la scène et mettre à jour le titre
            stage.setTitle("GameVault - " + username);
            stage.setScene(mainScene);
            stage.show();

            System.out.println("Connexion réussie pour : " + username);

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page principale : " + e.getMessage());
            e.printStackTrace();
        }
    }
}