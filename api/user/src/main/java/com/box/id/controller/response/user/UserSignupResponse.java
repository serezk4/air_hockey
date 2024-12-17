package com.box.id.controller.response.user;

import com.box.id.controller.request.user.UserSignupRequest;
import lombok.Value;

import java.io.Serializable;

/**
 * Registration response for {@link UserSignupRequest}
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */

@Value
public class UserSignupResponse implements Serializable {
    String username;
}
