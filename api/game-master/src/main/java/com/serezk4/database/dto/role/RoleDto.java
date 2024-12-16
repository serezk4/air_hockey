package com.serezk4.database.dto.role;

import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.serezk4.database.model.role.Role}
 */
@Value
public class RoleDto implements Serializable {
    Long id;
    @Length(message = "role.name.max:20", max = 20)
    String name;
    Long chatId;
    Boolean canWrite;
    Boolean canRead;
    Boolean canDelete;
    Boolean canInvite;
    Boolean canManage;
}
