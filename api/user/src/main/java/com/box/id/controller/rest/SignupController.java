package com.box.id.controller.rest;

import com.box.id.controller.request.user.UserSignupRequest;
import com.box.id.controller.response.ApiResponse;
import com.box.id.controller.response.user.UserSignupResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller responsible for handling user registration.
 * <p>
 * This controller provides an API endpoint to register a new user in the system by interacting
 * with Keycloak through its admin client. It processes the user's registration data, creates a new
 * user in Keycloak, and sets their password.
 * </p>
 * <p>
 * The registration request is validated using Jakarta Bean Validation annotations on the request body
 * ({@link UserSignupRequest}). The Keycloak admin client is used to manage users, and the response is
 * returned as a {@link Mono}, making this method reactive.
 * </p>
 *
 * <h2>Cross-Origin Support:</h2>
 * The {@code @CrossOrigin} annotation enables Cross-Origin Resource Sharing (CORS) for this controller,
 * allowing requests from any origin with credentials allowed.
 *
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/signup")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@Log4j2
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        allowCredentials = "true",
        methods = {RequestMethod.POST}
)
public class SignupController {
    /**
     * A reference to the Keycloak {@link UsersResource}, which is used to create and manage users.
     */
    UsersResource users;

    /**
     * Handles user registration requests.
     * <p>
     * This method processes a {@link UserSignupRequest} containing the user's registration details,
     * such as username, password, and email. It creates a new {@link UserRepresentation} in Keycloak
     * with the provided data and sets the user's password using {@link CredentialRepresentation}.
     * </p>
     * <p>
     * The method returns a reactive {@link Mono} containing a {@link ResponseEntity} with an
     * {@link ApiResponse.Body} wrapping a {@link UserSignupResponse}. If the user creation
     * fails, an error response is generated.
     * </p>
     *
     * <h3>Processing Steps:</h3>
     * <ul>
     *     <li>Creates a {@link UserRepresentation} and sets the username and email from the request.</li>
     *     <li>Sends the user creation request to Keycloak via {@code users.create(user)}.</li>
     *     <li>If the creation is successful, retrieves the user's ID and sets their password.</li>
     *     <li>Returns a success response with the username or an error message if the process fails.</li>
     * </ul>
     *
     * @param request the request containing the user's registration data, validated with {@link Valid}
     * @return a {@link Mono} containing a {@link ResponseEntity} with a response body of type {@link ApiResponse.Body}
     * wrapping a {@link UserSignupResponse}
     */
    @PostMapping
    public Mono<ResponseEntity<ApiResponse.Body<UserSignupResponse>>> signup(
            final @Valid @RequestBody UserSignupRequest request
    ) {
        return Mono.defer(() -> {
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
//            user.setUsername(request.getUsername());
            user.setUsername(UUID.randomUUID().toString());
            user.setEmail(request.getMail());
//            user.setAttributes(...);
            return Mono.just(user);
        }).map(user -> {
            // Create the user in Keycloak
            Response response = users.create(user);
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                log.error("Failed to create user: {}", response.getStatusInfo());
                return ApiResponse.bad(new UserSignupResponse(null), "user.signup.failed");
            }

            // Retrieve the user ID from the response
            String userId = CreatedResponseUtil.getCreatedId(response);

            // Set the user's password
            CredentialRepresentation credentials = new CredentialRepresentation();
            credentials.setTemporary(false);
            credentials.setType(CredentialRepresentation.PASSWORD);
            credentials.setValue(request.getPassword());

            // Reset the password for the newly created user
            users.get(userId).resetPassword(credentials);

            // Return a success response with the username
            return ApiResponse.ok(new UserSignupResponse(users.get(userId).toRepresentation().getUsername()));
        });

    }
}
