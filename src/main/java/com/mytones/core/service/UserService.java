package com.mytones.core.service;

import com.mytones.core.domain.player.Playlist;
import com.mytones.core.domain.user.User;
import com.mytones.core.repository.player.PlaylistRepository;
import com.mytones.core.repository.user.UserRepository;
import com.mytones.core.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    @Transactional
    public synchronized User createUser(String username, String firstName, String lastName) {
        final var user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);

        userRepository.save(user);

        final var favorites = new Playlist();
        favorites.setName(Constants.FAVORITES_PLAYLIST_KEY);
        favorites.setUser(user);
        user.getPlaylists().add(favorites);
        playlistRepository.save(favorites);

        return user;
    }
}
