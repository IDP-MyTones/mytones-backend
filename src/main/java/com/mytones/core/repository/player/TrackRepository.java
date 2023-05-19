package com.mytones.core.repository.player;

import com.mytones.core.domain.player.Artist;
import com.mytones.core.domain.player.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findAllByNameLikeIgnoreCase(String pattern);

    Page<Track> findAllByAlbumId(Long albumId, Pageable pageable);

    List<Track> findAllByArtistsContains(Artist artist);

    @Query("""
        SELECT t FROM Playlist p
        INNER JOIN p.tracks t
        INNER JOIN p.user u
        WHERE u.id = :userId and p.id = :playlistId
    """)
    Page<Track> findAllByPlaylistIdAndPlaylistUserId(Long playlistId, Long userId, Pageable pageable);
}
