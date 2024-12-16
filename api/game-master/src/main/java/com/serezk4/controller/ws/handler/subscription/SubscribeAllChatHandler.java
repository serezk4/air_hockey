package com.serezk4.controller.ws.handler.subscription;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import com.serezk4.controller.ws.subscription.ChatSubscriptionManager;
import com.serezk4.database.model.member.Member;
import com.serezk4.database.service.member.MemberService;
import com.serezk4.security.auth.model.CustomUserDetails;
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
public class SubscribeAllChatHandler implements WebSocketMessageHandler {
    ChatSubscriptionManager chatSubscriptionManager;
    MemberService memberService;

    @Override
    public boolean supports(String messageType) {
        return "subscribe_all".equalsIgnoreCase(messageType);
    }

    @Override
    public Mono<WebSocketMessage> handle(WebSocketSession session, String message) {
        var user = (CustomUserDetails) session.getAttributes().get("user");

        return memberService.findAllByUserId(user.getSub())
                .filter(Member::isEnableNotifications)
                .map(Member::getChatId)
                .flatMap(chatId -> chatSubscriptionManager.subscribe(chatId, session))
                .then(Mono.fromCallable(() -> session.textMessage("subscribed_all")));
    }
}
