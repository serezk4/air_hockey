package com.serezk4.database.model.chat;

import com.serezk4.database.model.message.Message;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "chats")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Chat {
    @Id
    Long id;

    @Column("title")
    @NotNull(message = "chat.title.required")
    String title;

    @Column("default_type_id")
    @NotNull(message = "chat.defaultTypeId.required")
    Long defaultTypeId;

    @Column("last_message_id")
    UUID lastMessageId;

    @Transient
    Message message;

    @Transient
    Type defaultType;
}
