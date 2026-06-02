package com.gamevault.util;

import java.util.prefs.Preferences;

public class UserSession {

    private static final Preferences PREFS = Preferences.userNodeForPackage(UserSession.class);
    private static final String KEY = "username";

    private static String username = "";

    public static void setUsername(String name) {
        username = name;
        PREFS.put(KEY, name); // persiste entre les sessions
    }

    public static String getUsername() {
        if (username == null || username.isBlank()) {
            username = PREFS.get(KEY, "");
        }
        return username;
    }

    public static boolean isLoggedIn() {
        return !getUsername().isBlank();
    }

    public static void logout() {
        username = "";
        PREFS.remove(KEY);
    }
}
