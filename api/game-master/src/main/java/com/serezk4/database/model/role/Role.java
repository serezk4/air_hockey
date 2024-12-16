package com.serezk4.database.model.role;

import com.serezk4.database.model.chat.Chat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Role {
    @Id Long id;
    
    @Column("name")
    @Length(max = 20, message = "role.name.max:20")
    @NotNull(message = "role.name.required")
    String name;

    @Column("chat_id")
    @NotNull(message = "role.chatId.required")
    Long chatId;

    @Transient
    Chat chat;
    
    @Column("can_write")
    @Builder.Default
    @NotNull(message = "role.canWrite.required")
    Boolean canWrite = true;
    
    @Column("can_read")
    @Builder.Default
    @NotNull(message = "role.canRead.required")
    Boolean canRead = true;
    
    @Column("can_delete")
    @Builder.Default
    @NotNull(message = "role.canDelete.required")
    Boolean canDelete = true;
    
    @Column("can_invite")
    @Builder.Default
    @NotNull(message = "role.canInvite.required")
    Boolean canInvite = true;
    
    @Column("can_manage")
    @Builder.Default
    @NotNull(message = "role.canManage.required")
    Boolean canManage = true;
}
