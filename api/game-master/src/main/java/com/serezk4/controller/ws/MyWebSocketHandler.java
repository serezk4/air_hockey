package com.serezk4.controller.ws;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import com.serezk4.controller.ws.listener.RabbitMQListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * WebSocket handler for managing WebSocket connections and message dispatching.
 */
@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyWebSocketHandler implements WebSocketHandler {

    RabbitTemplate rabbitTemplate;
    RabbitMQListener rabbitMQListener;
    Flux<WebSocketMessageHandler> handlers;

    @Override
    @NonNull
    public Mono<Void> handle(final WebSocketSession session) {
        log.info("WebSocket connection established, session ID: {}", session.getId());

        return Mono.just(session.getHandshakeInfo())
                .doOnNext(handshakeInfo -> setupSession(session))
                .then(handleIncomingMessages(session))
                .doOnError(error ->
                        log.error("Error in WebSocket session {}: {}", session.getId(), error.getMessage())
                );
    }

    private void setupSession(WebSocketSession session) {
        final String queueName = "session.queue." + UUID.randomUUID();
        session.getAttributes().put("queueName", queueName);

        log.info("Creating unique queue {} for WebSocket session ID: {}", queueName, session.getId());
        rabbitMQListener.addSession(session);
        setupQueueListener(queueName, session);
    }

    private Mono<Void> handleIncomingMessages(WebSocketSession session) {
        return session.receive()
                .doOnTerminate(() -> cleanupSession(session))
                .flatMap(message ->
                        dispatch(session, message.getPayloadAsText(StandardCharsets.UTF_8))
                )
                .doOnNext(response -> sendMessageToQueue(session, response))
                .then();
    }

    private void cleanupSession(WebSocketSession session) {
        rabbitMQListener.removeSession(session);
        removeQueueListener(session);
    }

    private void sendMessageToQueue(WebSocketSession session, WebSocketMessage response) {
        String queueName = (String) session.getAttributes().get("queueName");
        rabbitTemplate.convertAndSend(queueName, response.getPayloadAsText());
    }

    private void setupQueueListener(String queueName, WebSocketSession session) {
        rabbitTemplate.execute(channel -> {
            channel.queueDeclare(queueName, false, true, true, null);
            channel.basicConsume(queueName, true,
                    (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        rabbitMQListener.sendMessageToSession(session, message);
                    },
                    consumerTag ->
                            log.info("Consumer {} canceled for queue {}", consumerTag, queueName)
            );
            return null;
        });
    }

    private void removeQueueListener(WebSocketSession session) {
        String queueName = (String) session.getAttributes().get("queueName");
        if (queueName == null) {
            log.warn("No queue found for session {}", session.getId());
            return;
        }

        log.info("Removing queue listener and deleting queue for session {}: {}",
                session.getId(), queueName);

        rabbitTemplate.execute(channel -> {
            channel.queueDelete(queueName);
            return null;
        });
    }

    public Mono<WebSocketMessage> dispatch(WebSocketSession session, String message) {
        final String[] data = message.split("\\n\\n", 2);

        log.info("Dispatching message for session {}: Type: {}, Content: {}",
                session.getId(), data[0], data.length > 1 ? data[1] : "No content");

        return handlers
                .filter(handler -> handler.supports(data[0]))
                .next()
                .flatMap(handler -> handler.handle(session, data.length > 1 ? data[1] : null))
                .switchIfEmpty(
                        Mono.fromRunnable(() ->
                                log.warn("No handler found for type {} in session {}", data[0], session.getId())
                        )
                )
                .doOnError(error ->
                        log.error("Error dispatching message for session {}: {}",
                                session.getId(), error.getMessage())
                );
    }
}
