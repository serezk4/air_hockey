package com.serezk4.database.dto.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.serezk4.controller.gson.adapter.LocalDateTimeAdapter;
import com.serezk4.database.dto.member.MemberDto;
import com.serezk4.database.model.message.Message;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.serezk4.database.model.message.Message}
 */
@Value
@FieldDefaults(level = AccessLevel.PUBLIC)
public class MessageDto implements Serializable {
    UUID id;
    Long chatId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;
    MemberDto initiator;
    MemberDto victim;
    @Length(message = "message.text.length", min = 1, max = 1000)
    String text;
    Message.Type type;

    @Override
    public String toString() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create()
                .toJson(this);
    }
}
