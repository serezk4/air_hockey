package com.serezk4.database.mapper.chat.impl;

import com.serezk4.database.dto.chat.ChatDto;
import com.serezk4.database.mapper.chat.ChatMapper;
import com.serezk4.database.mapper.message.MessageMapper;
import com.serezk4.database.model.chat.Chat;
import com.serezk4.database.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatMapperImpl implements ChatMapper {
    MessageService messageService;
    MessageMapper messageMapper;

    @Override
    public Chat toEntity(ChatDto chatDto) {
        return null;
    }

    @Override
    public Mono<ChatDto> toDto(Chat chat) {
        if (chat == null)
            return Mono.empty();

        if (chat.getLastMessageId() == null)
            return Mono.just(new ChatDto(chat.getId(), chat.getTitle(), null));

        return messageService.findById(chat.getLastMessageId())
                .flatMap(messageMapper::toDto)
                .switchIfEmpty(Mono.empty())
                .map(_lastMessage -> new ChatDto(chat.getId(), chat.getTitle(), _lastMessage));
    }

    @Override
    public Chat partialUpdate(ChatDto chatDto, Chat chat) {
        if (chatDto == null) {
            return chat;
        }

        if (chatDto.getId() != null) {
            chat.setId(chatDto.getId());
        }
        if (chatDto.getTitle() != null) {
            chat.setTitle(chatDto.getTitle());
        }
        if (chatDto.getLastMessage() != null) {
            chat.setLastMessageId(chatDto.getLastMessage().getId());
        }

        return chat;
    }
}
