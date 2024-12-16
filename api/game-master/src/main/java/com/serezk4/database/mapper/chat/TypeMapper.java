package com.serezk4.database.mapper.chat;

import com.serezk4.database.dto.chat.TypeDto;
import com.serezk4.database.model.chat.Type;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TypeMapper {
    Type toEntity(TypeDto typeDto);

    TypeDto toDto(Type type);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Type partialUpdate(TypeDto typeDto, @MappingTarget Type type);
}
