package com.gamevault.repository;

import com.gamevault.model.Game;
import com.gamevault.model.GameStatus;
import com.gamevault.model.Platform;
import com.gamevault.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate de GameRepository.
 *
 * Le corps de chaque méthode sera complété lors de l'activation de la couche BDD.
 * Structure et signatures sont déjà définitives.
 */
public class GameRepositoryImpl implements GameRepository {

    @Override
    public Game save(Game game) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(game);
            tx.commit();
            return game;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erreur lors de la sauvegarde du jeu", e);
        }
    }

    @Override
    public Optional<Game> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Game.class, id));
        }
    }

    @Override
    public List<Game> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Game ORDER BY title", Game.class).list();
        }
    }

    @Override
    public List<Game> findByTitle(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Game WHERE LOWER(title) LIKE :title ORDER BY title",
                    Game.class
            ).setParameter("title", "%" + title.toLowerCase() + "%").list();
        }
    }

    @Override
    public List<Game> findByPlatform(Platform platform) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Game WHERE platform = :platform ORDER BY title",
                    Game.class
            ).setParameter("platform", platform).list();
        }
    }

    @Override
    public List<Game> findByStatus(GameStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Game WHERE status = :status ORDER BY title",
                    Game.class
            ).setParameter("status", status).list();
        }
    }

    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Game game = session.get(Game.class, id);
            if (game != null) {
                session.remove(game);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erreur lors de la suppression du jeu", e);
        }
    }
}
