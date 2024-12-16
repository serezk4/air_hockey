package com.serezk4.controller.rest.response.chat;

import com.serezk4.database.dto.message.MessageDto;
import lombok.Value;

import java.util.List;

@Value
public class ChatMessageResponse {
    List<MessageDto> messages;
    long size;
}
