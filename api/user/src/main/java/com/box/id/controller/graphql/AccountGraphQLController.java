package com.box.id.controller.graphql;

import com.box.id.controller.response.user.UserDetailsResponse;
import com.box.id.controller.response.user.UserSecurityDetailsResponse;
import com.box.id.security.auth.model.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class AccountGraphQLController {
    UsersResource usersResource;
    RealmResource realmResource;

    @QueryMapping
    public Mono<UserDetailsResponse> me(
            @AuthenticationPrincipal Mono<CustomUserDetails> userDetails
    ) {
        return userDetails.map(UserDetailsResponse::new);
    }

    @QueryMapping
    public Mono<UserSecurityDetailsResponse> meSecurity(
            @AuthenticationPrincipal Mono<CustomUserDetails> userDetails
    ) {
        return userDetails
                .flatMap(user -> Mono.fromCallable(() -> usersResource.get(user.getSub())))
                .map(user -> new UserSecurityDetailsResponse(
                        user.getUserSessions(),
                        user.credentials().stream()
                                .map(UserSecurityDetailsResponse.CredentialsDto::new)
                                .toList()
                ));
    }
}
