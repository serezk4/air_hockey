package com.serezk4.database.dto.game;

import com.serezk4.database.model.game.Game;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.serezk4.database.model.game.Game}
 */
@Value
public class GameDto implements Serializable {
    UUID uuid;
    String playerSub1;
    String playerSub2;
    Game.GameStatus gameStatus;
    String gameResult;
    GameRulesetDto ruleset;
    LocalDateTime createdAt;
    LocalDateTime endedAt;
}
