package com.serezk4.database.mapper.chat;

import com.serezk4.database.dto.chat.ChatDto;
import com.serezk4.database.model.chat.Chat;
import org.mapstruct.*;
import reactor.core.publisher.Mono;

public interface ChatMapper {
    Chat toEntity(ChatDto chatDto);

    Mono<ChatDto> toDto(Chat chat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Chat partialUpdate(ChatDto chatDto, @MappingTarget Chat chat);
}
