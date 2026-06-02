package com.gamevault.model;

/**
 * Statuts possibles d'un jeu dans la collection.
 */
public enum GameStatus {

    TO_PLAY("À faire"),
    IN_PROGRESS("En cours"),
    COMPLETED("Terminé"),
    COLLECTION("Collection");

    private final String label;

    GameStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
