package com.mytones.core.repository.player;

import com.mytones.core.domain.player.Playlist;
import com.mytones.core.domain.player.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findAllByNameLikeAndUserId(String name, Long userId);

    List<Playlist> findAllByNameLikeAndNameNotAndUserId(String name, String favoritePlaylistName, Long userId);

    @Query("""
        SELECT t FROM Playlist p
        JOIN p.tracks t
        JOIN p.user u
        WHERE p.name = '__USER__FAVORITES__' and u.id = :userId  and t.id = :trackId
    """)
    Optional<Track> findTrackInFavorites(Long userId, Long trackId);
}
