package com.gamevault.model;

/**
 * Plateformes supportées par GameVault.
 */
public enum Platform {

    PC("PC"),
    PLAYSTATION_4("PlayStation 4"),
    PLAYSTATION_5("PlayStation 5"),
    XBOX_ONE("Xbox One"),
    XBOX_SERIES("Xbox Series"),
    NINTENDO_SWITCH("Nintendo Switch"),
    GAME_BOY("Game Boy"),
    SUPER_NINTENDO("Super Nintendo"),
    SEGA_SATURN("Sega Saturn"),
    DREAMCAST("Dreamcast"),
    OTHER("Autre");

    private final String label;

    Platform(String label) {
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
