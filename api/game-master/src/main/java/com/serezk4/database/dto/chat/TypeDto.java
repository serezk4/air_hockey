package com.serezk4.database.dto.chat;

import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.serezk4.database.model.chat.Type}
 */
@Value
public class TypeDto implements Serializable {
    Long id;
    @Length(message = "type.name.max:50", max = 50)
    String name;
    String userId;
    Integer maxMembers;
    Long defaultRoleId;
}
