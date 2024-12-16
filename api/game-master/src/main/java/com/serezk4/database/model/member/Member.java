package com.serezk4.database.model.member;

import com.serezk4.database.model.chat.Chat;
import com.serezk4.database.model.role.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "members")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Member {
    @Id
    Long id;

    @NotNull(message = "member.chatId.required")
    String userId;

    @NotNull(message = "member.chatId.required")
    Long chatId;

    @Column("preferred_name")
    String preferredName;

    @Transient
    Chat chat;

    @Column("role_id")
    @NotNull(message = "member.roleId.required")
    Long roleId;

    @Column("inviter_id")
    Long inviterId;

    @Column("enable_notifications")
    @Builder.Default
    boolean enableNotifications = true;

    @Transient
    Role role;
}
