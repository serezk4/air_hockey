package com.serezk4.controller.ws.handler;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public interface WebSocketMessageHandler {
    boolean supports(String messageType);

    Mono<WebSocketMessage> handle(WebSocketSession session, String message);
}
