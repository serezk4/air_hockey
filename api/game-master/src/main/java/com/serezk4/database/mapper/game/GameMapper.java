package com.serezk4.database.mapper.game;

import com.serezk4.database.dto.game.GameDto;
import com.serezk4.database.model.game.Game;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameMapper {
    Game toEntity(GameDto gameDto);

    GameDto toDto(Game game);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Game partialUpdate(GameDto gameDto, @MappingTarget Game game);
}
