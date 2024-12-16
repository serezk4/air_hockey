package com.serezk4.controller.ws.handler.subscription;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import com.serezk4.controller.ws.subscription.ChatSubscriptionManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SubscribeChatHandler implements WebSocketMessageHandler {
    ChatSubscriptionManager chatSubscriptionManager;

    @Override
    public boolean supports(String messageType) {
        return "subscribe".equalsIgnoreCase(messageType);
    }

    @Override
    public Mono<WebSocketMessage> handle(WebSocketSession session, String message) {
        return chatSubscriptionManager.subscribe(Long.parseLong(message), session)
                .then(Mono.fromCallable(() -> session.textMessage("subscribed")));
    }
}
