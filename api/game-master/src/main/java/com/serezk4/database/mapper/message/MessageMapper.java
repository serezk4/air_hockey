package com.serezk4.database.mapper.message;

import com.serezk4.database.dto.message.MessageDto;
import com.serezk4.database.model.message.Message;
import org.mapstruct.*;
import reactor.core.publisher.Mono;

public interface MessageMapper {
    Message toEntity(MessageDto messageDto);

    Mono<MessageDto> toDto(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Message partialUpdate(MessageDto messageDto, @MappingTarget Message message);
}
