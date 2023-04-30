package com.mytones.core.repository.player;

import com.mytones.core.domain.player.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByNameLikeIgnoreCase(String pattern);
}
