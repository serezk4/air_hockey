package com.serezk4.database.model.game;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "games")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Game {
    /**
     * Unique identifier for the game.
     */
    @Id
    UUID uuid;

    /**
     * Sub identifier of the first player.
     */
    @Column("player_sub_1")
    String playerSub1;

    /**
     * Sub identifier of the second player.
     */
    @Column("player_sub_2")
    String playerSub2;

    /**
     * Current status of the game.
     */
    @Column("game_status")
    GameStatus gameStatus;

    /**
     * Result of the game.
     */
    @Column("game_result")
    String gameResult;

    /**
     * Ruleset and physical parameters of the game (serialized as JSON).
     */
    @Column("ruleset")
    GameRuleset ruleset;

    /**
     * Timestamp of when the game was created.
     */
    @Column("created_at")
    LocalDateTime createdAt;

    /**
     * Timestamp of when the game ended.
     */
    @Column("ended_at")
    LocalDateTime endedAt;

    public enum GameStatus {
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        CANCELED
    }
}
