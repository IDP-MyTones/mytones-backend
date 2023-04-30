package com.mytones.core.repository.player;

import com.mytones.core.domain.player.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findAllByNameLikeIgnoreCase(String pattern);
}
