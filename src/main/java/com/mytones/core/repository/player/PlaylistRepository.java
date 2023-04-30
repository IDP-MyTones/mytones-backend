package com.mytones.core.repository.player;

import com.mytones.core.domain.player.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
