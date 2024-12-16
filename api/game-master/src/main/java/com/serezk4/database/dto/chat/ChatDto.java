package com.serezk4.database.dto.chat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.serezk4.controller.gson.adapter.LocalDateTimeAdapter;
import com.serezk4.database.dto.message.MessageDto;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.serezk4.database.model.chat.Chat}
 */
@Value
public class ChatDto implements Serializable {
    Long id;
    String title;
    MessageDto lastMessage;

    @Override
    public String toString() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create()
                .toJson(this);
    }
}
