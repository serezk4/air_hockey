package com.serezk4.controller.ws.handler.send;

import com.serezk4.controller.ws.handler.WebSocketMessageHandler;
import com.serezk4.controller.ws.subscription.ChatSubscriptionManager;
import com.serezk4.database.mapper.message.impl.MessageMapperImpl;
import com.serezk4.database.model.message.Message;
import com.serezk4.database.service.chat.ChatService;
import com.serezk4.database.service.member.MemberService;
import com.serezk4.database.service.message.MessageService;
import com.serezk4.security.auth.model.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SendMessageHandler implements WebSocketMessageHandler {
    ChatService chatService;
    MemberService memberService;
    MessageService messageService;
    MessageMapperImpl messageMapper;
    ChatSubscriptionManager chatSubscriptionManager;

    @Override
    public boolean supports(String messageType) {
        return "send".equalsIgnoreCase(messageType);
    }

    @Override
    public Mono<WebSocketMessage> handle(WebSocketSession session, String message) {
        var user = (CustomUserDetails) session.getAttributes().get("user");
        String[] data = message.split("\n", 2);

        if (data.length != 2) return Mono.just(session.textMessage("error.send.message.invalid"));

        return chatService.findById(Long.parseLong(data[0]))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("error.send.chat.not.found")))
                .flatMap(chat -> memberService.findByUserIdAndChatId(user.getSub(), chat.getId()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("error.send.member.not.found")))
                .flatMap(member -> {
                    if (member.getPreferredName() != null) return Mono.just(member);
                    return memberService.save(member.setPreferredName(user.getPreferredUsername()));
                })
                .map(member -> Message.builder()
                        .chatId(member.getChatId())
                        .text(data[1])
                        .initiatorId(member.getId())
                        .build())
                .flatMap(messageService::save)
                .flatMap(messageMapper::toDto)
                .flatMap(messageDto -> chatSubscriptionManager
                        .notifySubscribers(messageDto.getChatId(), messageDto.toString())
                        .then(Mono.just(messageDto)))
                .map(Object::toString)
                .map(session::textMessage);
    }
}
