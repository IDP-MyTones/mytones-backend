package com.mytones.core.web.dto.user;

import com.mytones.core.domain.user.Role;
import com.mytones.core.domain.user.User;

public record SimpleUserDto(Long id, String username, String lastName, String firstName, Long imageId, Role role) {

    public SimpleUserDto(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getLastName(),
                user.getFirstName(),
                user.getImage() == null ? null : user.getImage().getId(),
                user.getRole()
        );
    }
}
