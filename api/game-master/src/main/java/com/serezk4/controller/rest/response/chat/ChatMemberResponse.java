package com.serezk4.controller.rest.response.chat;

import com.serezk4.database.dto.member.MemberDto;
import lombok.Value;

@Value
public class ChatMemberResponse {
    MemberDto member;
}
