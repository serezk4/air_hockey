package com.serezk4.controller.ws.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class RabbitMQListener {

    private final Map<WebSocketSession, Sinks.Many<String>> sessionSinks = new ConcurrentHashMap<>();

    public void addSession(WebSocketSession session) {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        sessionSinks.put(session, sink);

        session.send(sink.asFlux().map(session::textMessage))
                .doOnError(error -> log.error("Error sending message to WebSocket session {}: {}",
                        session.getId(), error.getMessage()))
                .subscribe();
    }

    public void removeSession(WebSocketSession session) {
        Sinks.Many<String> sink = sessionSinks.remove(session);
        if (sink == null) return;
        sink.tryEmitComplete();
    }

    public void sendMessageToSession(WebSocketSession session, String message) {
        Sinks.Many<String> sink = sessionSinks.get(session);
        if (sink == null) return;
        sink.tryEmitNext(message).orThrow();
    }
}
