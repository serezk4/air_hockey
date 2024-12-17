package com.serezk4.database.repository.game;

import com.serezk4.database.model.game.Game;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends ReactiveCrudRepository<Game, UUID> {
}
