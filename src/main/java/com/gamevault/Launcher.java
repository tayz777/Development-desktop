package com.gamevault;

/**
 * Point d'entrée réel de l'application.
 *
 * Cette classe séparée est nécessaire pour la compatibilité avec les JAR
 * exécutables : une classe qui étend Application ne peut pas être utilisée
 * directement comme Main-Class dans le manifest d'un fat-JAR.
 */
public class Launcher {

    public static void main(String[] args) {
        App.main(args);
    }
}
