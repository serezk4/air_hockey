package com.serezk4.controller.ws.subscription;

import com.serezk4.security.auth.model.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class ChatSubscriptionManager {

    Map<Long, Set<WebSocketSession>> chatSubscriptions = new ConcurrentHashMap<>();
    RabbitTemplate rabbitTemplate;

    public Mono<Void> subscribe(Long chatId, WebSocketSession session) {
        return Mono.fromRunnable(() -> chatSubscriptions
                .computeIfAbsent(chatId, id -> ConcurrentHashMap.newKeySet())
                .add(session)
        );
    }

    public Mono<Void> subscribe(Long chatId, CustomUserDetails user) {
        return Mono.fromRunnable(() -> {
            log.info("Subscribing user {} to chatId {}", user.getPreferredUsername(), chatId);
            chatSubscriptions.values().forEach(sessions -> sessions.forEach(session -> {
                CustomUserDetails sessionUser = (CustomUserDetails) session.getAttributes().get("user");
                log.info("Session user: {}", sessionUser);

                if (!user.equals(sessionUser)) return;

                chatSubscriptions.computeIfAbsent(chatId, id -> ConcurrentHashMap.newKeySet()).add(session);
                log.info(chatSubscriptions.get(chatId));
                log.info("Subscribed user {} to chatId {}", user.getPreferredUsername(), chatId);
            }));
        });
    }

    public void unsubscribe(Long chatId, WebSocketSession session) {
        Set<WebSocketSession> subscribers = chatSubscriptions.get(chatId);
        if (subscribers != null) {
            subscribers.remove(session);
            if (subscribers.isEmpty()) {
                chatSubscriptions.remove(chatId);
            }
        }
    }

    public Mono<Boolean> notifySubscribers(Long chatId, String notification) {
        Set<WebSocketSession> subscribers = chatSubscriptions.get(chatId);
        if (subscribers == null) return Mono.just(false);

        for (WebSocketSession session : subscribers) {
            rabbitTemplate.convertAndSend(
                    (String) session.getAttributes().get("queueName"),
                    notification
            );
        }

        return Mono.just(true);
    }
}
