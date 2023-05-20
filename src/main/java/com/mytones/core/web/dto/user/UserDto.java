package com.mytones.core.web.dto.user;

import com.mytones.core.domain.user.User;

public record UserDto(Long id,
                      String firstName,
                      String lastName,
                      String username) {

    public UserDto(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername()
            );
    }
}
