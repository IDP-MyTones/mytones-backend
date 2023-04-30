package com.mytones.core.web.dto.user;

import com.mytones.core.domain.user.User;
import com.mytones.core.web.dto.player.PlaylistDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record UserDto(Long id,
                      String firstName,
                      String lastName,
                      Long imageId,
                      String username,
                      List<PlaylistDto> playlists,
                      LocalDate birthday) {

    public UserDto(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getImage() == null ? null : user.getImage().getId(),
                user.getUsername(),
                user.getPlaylists().stream().map(PlaylistDto::new).collect(Collectors.toList()),
                user.getBirthday()
            );
    }
}
