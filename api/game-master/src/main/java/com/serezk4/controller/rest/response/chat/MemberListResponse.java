package com.serezk4.controller.rest.response.chat;

import com.serezk4.database.dto.member.MemberDto;
import lombok.Value;

import java.util.List;

@Value
public class MemberListResponse {
    List<MemberDto> members;
    long size;
}
