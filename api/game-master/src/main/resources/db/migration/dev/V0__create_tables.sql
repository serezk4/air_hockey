CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE games
(
    uuid         UUID PRIMARY KEY,
    player_sub_1 VARCHAR(255)                NOT NULL,
    player_sub_2 VARCHAR(255)                NOT NULL,
    game_status  VARCHAR(50)                 NOT NULL,
    game_result  VARCHAR(255),
    ruleset      JSONB,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ended_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT check_game_status CHECK (
        game_status IN ('CREATED', 'IN_PROGRESS', 'COMPLETED', 'CANCELED')
        )
);

CREATE INDEX idx_games_status ON games (game_status);
CREATE INDEX idx_games_result ON games (game_result);
CREATE INDEX idx_games_created_at ON games (created_at);
CREATE INDEX idx_games_player1 ON games (player_sub_1);
CREATE INDEX idx_games_player2 ON games (player_sub_2);
CREATE INDEX idx_games_ruleset ON games USING GIN (ruleset);