package com.serezk4.database.mapper.member;

import com.serezk4.database.dto.member.MemberDto;
import com.serezk4.database.model.member.Member;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    Member toEntity(MemberDto memberDto);

    MemberDto toDto(Member member);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Member partialUpdate(MemberDto memberDto, @MappingTarget Member member);
}
