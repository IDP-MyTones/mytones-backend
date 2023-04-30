package com.mytones.core.repository.user;

import com.mytones.core.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"playlists", "image"})
    Optional<User> findWithProfileByUsername(String username);
}
