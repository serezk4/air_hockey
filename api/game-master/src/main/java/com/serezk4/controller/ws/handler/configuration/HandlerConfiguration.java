package com.serezk4.controller.ws.handler.configuration;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
public class HandlerConfiguration {
    @Bean
    public Flux<WebSocketMessageHandler> handlers(List<WebSocketMessageHandler> handlers) {
        return Flux.fromIterable(handlers);
    }
}
