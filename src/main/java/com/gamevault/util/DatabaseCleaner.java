package com.gamevault.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseCleaner {

    /**
     * Vide intégralement les tables de la base de données.
     */
    public static void cleanDatabase() {
        Transaction transaction = null;
        System.out.println("⚠️ Nettoyage complet de la base de données en cours...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 1. Démarrage de la transaction
            transaction = session.beginTransaction();

            // 2. Suppression de tous les enregistrements de l'entité Game.
            // Grâce à @ElementCollection, cela vide AUSSI la table des plateformes liée !
            int deletedGamesCount = session.createMutationQuery("delete from Game").executeUpdate();

            // 3. Validation des suppressions dans le fichier SQLite
            transaction.commit();

            System.out.println("✅ Base de données vidée avec succès !");
            System.out.println("🗑️ Nombre de jeux supprimés : " + deletedGamesCount);

        } catch (Exception e) {
            // En cas d'erreur, on annule tout pour ne pas corrompre le fichier SQLite
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            System.err.println("❌ Échec du nettoyage de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}