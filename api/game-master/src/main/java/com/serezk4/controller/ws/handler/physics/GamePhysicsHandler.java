package com.serezk4.controller.ws.handler.physics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import com.serezk4.controller.ws.subscription.GameSubscriptionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
@RequiredArgsConstructor
public class GamePhysicsHandler implements WebSocketMessageHandler {

    private final GameSubscriptionManager subscriptionManager;
    private final ObjectMapper objectMapper;

    private final Map<String, GameState> gameStates = new ConcurrentHashMap<>();

    @Override
    public boolean supports(String messageType) {
        return "GAME_UPDATE".equals(messageType);
    }

    @Override
    public Mono<WebSocketMessage> handle(WebSocketSession session, String message) {
        try {
            GameUpdateRequest request = objectMapper.readValue(message, GameUpdateRequest.class);
            String gameId = request.getGameId();

            GameState state = gameStates.computeIfAbsent(gameId, id -> new GameState());
            state.updatePlayerPosition(request.getPlayerId(), request.getPosition());

            state.applyPhysics();

            String stateJson = objectMapper.writeValueAsString(state);
            subscriptionManager.notifySubscribers(gameId, stateJson);

            return Mono.empty();
        } catch (Exception e) {
            log.error("Error handling game update: {}", e.getMessage());
            return Mono.empty();
        }
    }

    // DTO для входящего сообщения
    private static class GameUpdateRequest {
        String gameId;
        String playerId;
        double position;

        public String getGameId() { return gameId; }
        public String getPlayerId() { return playerId; }
        public double getPosition() { return position; }
    }

    // Состояние игры
    private static class GameState {
        private final Map<String, Double> playerPositions = new ConcurrentHashMap<>();

        void updatePlayerPosition(String playerId, double position) {
            playerPositions.put(playerId, position);
        }

        void applyPhysics() {
            playerPositions.replaceAll((player, pos) -> pos + 1); // Пример физики: движение вперёд
        }

        public Map<String, Double> getPlayerPositions() {
            return playerPositions;
        }
    }
}
