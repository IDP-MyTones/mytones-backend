package com.mytones.core.web.dto.user;

import com.mytones.core.domain.user.Role;
import com.mytones.core.domain.user.User;
import com.mytones.core.web.dto.player.PlaylistDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record UserDto(Long id,
                      String firstName,
                      String lastName,
                      String username,
                      Role role) {

    public UserDto(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getRole()
            );
    }
}
