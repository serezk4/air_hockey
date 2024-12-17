package com.serezk4.controller.ws.subscription;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GameSubscriptionManager {

    Map<String, Set<WebSocketSession>> gameSubscriptions = new ConcurrentHashMap<>();

    public void subscribe(String gameId, WebSocketSession session) {
        gameSubscriptions
                .computeIfAbsent(gameId, id -> ConcurrentHashMap.newKeySet())
                .add(session);
        log.info("Subscribed session {} to game {}", session.getId(), gameId);
    }

    public void unsubscribe(String gameId, WebSocketSession session) {
        Set<WebSocketSession> subscribers = gameSubscriptions.get(gameId);
        if (subscribers != null) {
            subscribers.remove(session);
            if (subscribers.isEmpty()) {
                gameSubscriptions.remove(gameId);
            }
        }
        log.info("Unsubscribed session {} from game {}", session.getId(), gameId);
    }

    public void notifySubscribers(String gameId, String update) {
        Set<WebSocketSession> subscribers = gameSubscriptions.get(gameId);
        if (subscribers == null) return;

        for (WebSocketSession session : subscribers) {
            session.send(Mono.just(session.textMessage(update))).subscribe();
        }
    }
}
