package com.gamevault.repository;

import com.gamevault.model.Game;
import com.gamevault.model.GameStatus;
import com.gamevault.model.Platform;

import java.util.List;
import java.util.Optional;

/**
 * Contrat d'accès aux données pour les jeux.
 */
public interface GameRepository {

    Game save(Game game);

    Optional<Game> findById(Long id);

    List<Game> findAll();

    List<Game> findByTitle(String title);

    List<Game> findByPlatform(Platform platform);

    List<Game> findByStatus(GameStatus status);

    void delete(Long id);
}
