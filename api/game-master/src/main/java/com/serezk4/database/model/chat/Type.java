package com.serezk4.database.model.chat;

import com.serezk4.database.model.role.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "types")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class Type {
    @Id
    Long id;

    @Column("name")
    @Length(max = 50, message = "type.name.max:50")
    @NotNull(message = "type.name.required")
    String name;

    @Column("user_id")
    String userId;

    @Column("max_members")
    @Builder.Default
    @NotNull(message = "type.maxMembers.required")
    Integer maxMembers = 2;

    @Column("default_role_id")
    Long defaultRoleId;

    @Transient
    Role defaultRole;
}
