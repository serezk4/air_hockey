package com.box.id.controller.response.user;

import com.box.id.security.auth.model.CustomUserDetails;
import lombok.Value;

@Value
public class UserDetailsResponse {
    CustomUserDetails userDetails;
}
