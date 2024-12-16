package com.serezk4.controller.rest.response.chat;

import com.serezk4.database.dto.chat.ChatDto;
import lombok.Value;

import java.util.List;

@Value
public class ChatListResponse {
    List<ChatDto> chats;
    long size;
}
