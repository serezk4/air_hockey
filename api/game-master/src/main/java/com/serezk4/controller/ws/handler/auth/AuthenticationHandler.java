package com.serezk4.controller.ws.handler.auth;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import com.serezk4.security.auth.converter.CustomJwtAuthenticationConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationHandler implements WebSocketMessageHandler {
    CustomJwtAuthenticationConverter customJwtAuthenticationConverter;
    JwtDecoder jwtDecoder;

    @Override
    public boolean supports(String messageType) {
        return "auth".equalsIgnoreCase(messageType);
    }

    @Override
    public Mono<WebSocketMessage> handle(WebSocketSession session, String token) {
        return Mono.fromCallable(() -> jwtDecoder.decode(token))
                .flatMap(jwt -> customJwtAuthenticationConverter.convert(jwt)
                        .doOnNext(authentication -> session.getAttributes().put("user", authentication.getPrincipal()))
                        .map(auth -> session.textMessage("SET%Token verified"))
                )
                .onErrorResume(JwtException.class, ex -> {
                    log.error("Token verification failed", ex);
                    return Mono.just(session.textMessage("SET%Invalid token"));
                });
    }
}
