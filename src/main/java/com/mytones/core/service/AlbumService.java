package com.mytones.core.service;

import com.mytones.core.repository.player.AlbumRepository;
import com.mytones.core.repository.player.TrackRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    public void deleteTrack(Long albumId, Long trackId) {
        final var album = albumRepository.findById(albumId).orElseThrow(NotFoundException::new);
        final var track = trackRepository.findById(trackId).orElseThrow(NotFoundException::new);
        album.removeTrack(track);
    }

    public void delete(Long albumId) {
        final var album = albumRepository.findById(albumId).orElseThrow(NotFoundException::new);
        albumRepository.delete(album);
    }

}
