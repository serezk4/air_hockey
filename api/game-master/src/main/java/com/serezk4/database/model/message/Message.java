package com.serezk4.database.model.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.serezk4.database.model.chat.Chat;
import com.serezk4.database.model.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Table(name = "default_messages")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Message {
    @Id
    UUID id;

    @Column("chat_id")
    Long chatId;

    @Transient
    Chat chat;

    @Column("created_at")
    @NotNull(message = "systemMessage.createdAt.required")
    @Builder.Default
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt = LocalDateTime.now(ZoneId.systemDefault());

    @Column("initiator_id")
    Long initiatorId;

    @Transient
    Member initiator;

    @Column("victim_id")
    Long victimId;

    @Transient
    Member victim;

    @Column("text")
    @NotNull(message = "message.text.required")
    @NotBlank(message = "message.text.blank")
    @Length(min = 1, max = 1000, message = "message.text.length")
    String text;

    @Column("type")
    @NotNull(message = "message.type.required")
    @Builder.Default
    Type type = Type.TEXT;

    public enum Type {
        MEMBER_ADD, MEMBER_REMOVE, MEMBER_MODIFY, TEXT, CHAT_CREATE;
    }
}
