package com.mytones.core.repository.player;

import com.mytones.core.domain.player.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findAllByNameLikeIgnoreCase(String pattern);
}
