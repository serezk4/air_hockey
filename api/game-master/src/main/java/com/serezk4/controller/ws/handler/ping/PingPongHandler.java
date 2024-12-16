package com.serezk4.controller.ws.handler.ping;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class PingPongHandler implements WebSocketMessageHandler {

    @Override
    public boolean supports(String messageType) {
        return "ping".equalsIgnoreCase(messageType);
    }

    @Override
    public Mono<WebSocketMessage> handle(WebSocketSession session, String message) {
        log.info("Session attributes: {}", session.getAttributes());
        return Mono.just(session.textMessage(message));
    }
}
