package com.gamevault.service;

import com.gamevault.model.Game;
import com.gamevault.model.GameStatus;
import com.gamevault.model.Platform;
import com.gamevault.repository.GameRepository;

import java.util.List;
import java.util.Optional;

/**
 * Couche service : contient la logique métier et délègue la persistance
 * au repository.
 */
public class GameService {

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public Game addGame(Game game) {
        validateGame(game);
        return repository.save(game);
    }

    public Game updateGame(Game game) {
        validateGame(game);
        return repository.save(game);
    }

    public Optional<Game> getGame(Long id) {
        return repository.findById(id);
    }

    public List<Game> getAllGames() {
        return repository.findAll();
    }

    public List<Game> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            return repository.findAll();
        }
        return repository.findByTitle(title.trim());
    }

    public List<Game> filterByPlatform(Platform platform) {
        if (platform == null) {
            return repository.findAll();
        }
        return repository.findByPlatform(platform);
    }

    public List<Game> filterByStatus(GameStatus status) {
        if (status == null) {
            return repository.findAll();
        }
        return repository.findByStatus(status);
    }

    public void deleteGame(Long id) {
        repository.delete(id);
    }

    // ── Validation ────────────────────────────────────────────────────────

    private void validateGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Le jeu ne peut pas être null.");
        }
        if (game.getTitle() == null || game.getTitle().isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire.");
        }
        if (game.getPlatform() == null) {
            throw new IllegalArgumentException("La plateforme est obligatoire.");
        }
        if (game.getPersonalRating() != null
                && (game.getPersonalRating() < 0 || game.getPersonalRating() > 10)) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 10.");
        }
    }
}
