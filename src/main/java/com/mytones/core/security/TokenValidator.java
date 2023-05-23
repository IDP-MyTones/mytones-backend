package com.mytones.core.security;

import com.mytones.core.repository.user.UserRepository;
import com.mytones.core.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
class TokenValidator {


    private static final RestTemplate restTemplate = new RestTemplate();

    private final UserRepository userRepository;
    private final UserService userService;
    private final String authServerUrl;

    public TokenValidator(UserRepository userRepository,
                          UserService userService,
                          @Value("${AUTH_SERVER_URL:''}") String authServerUrl) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authServerUrl = authServerUrl;
    }


    public UserDto extract(final String token) {
        final var dto = fetch(token);

        if (userRepository.findByUsername(dto.username).isEmpty()) {
            userService.createUser(dto.username, dto.firstName, dto.lastName);
        }

        return dto;
    }

    private UserDto fetch(String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + token);


        final HttpEntity<Void> entity = new HttpEntity<>(headers);
        final var response = restTemplate.exchange(authServerUrl, HttpMethod.GET, entity, UserDto.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AccessDeniedException("Invalid token");
        }

        return response.getBody();
    }


    @Getter
    @Setter
    public static class UserDto {

        private String username;
        private String role;
        private String firstName;
        private String lastName;
    }
}
