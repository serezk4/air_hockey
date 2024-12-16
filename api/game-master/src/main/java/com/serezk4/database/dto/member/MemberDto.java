package com.serezk4.database.dto.member;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.serezk4.database.model.member.Member}
 */
@Value
public class MemberDto implements Serializable {
    Long id;
    String userId;
    String preferredName;
    Long chatId;
}
