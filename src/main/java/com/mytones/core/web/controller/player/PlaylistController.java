package com.mytones.core.web.controller.player;

import com.mytones.core.domain.file.ImageFile;
import com.mytones.core.domain.player.Playlist;
import com.mytones.core.repository.file.ImageRepository;
import com.mytones.core.repository.player.PlaylistRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.security.CurrentUserHolder;
import com.mytones.core.storage.FileStore;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.player.PlaylistDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.ws.rs.NotFoundException;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(Constants.BASE_URL + "/playlists")
@RequiredArgsConstructor
class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;
    private final CurrentUserHolder currentUserHolder;

    @GetMapping("/{id}")
    @Transactional
    public PlaylistDto get(@PathVariable long id) {
        var playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));

        return new PlaylistDto(playlist);
    }

    @PostMapping
    @Transactional
    public Long create(@RequestBody String name) {

        var playlist = new Playlist();
        playlist.setName(name);
        currentUserHolder.get().addPlaylist(playlist);

        playlistRepository.save(playlist);
        return playlist.getId();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable long id) {
        var playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));
        if (playlist.getImage() != null) {
            fileStore.unassociate(playlist.getImage());
        }
        currentUserHolder.get().removePlaylist(playlist);
    }

    @PutMapping("/{id}")
    @Transactional
    public void update(@PathVariable long id, @RequestBody String name) {
        playlistRepository.findById(id).ifPresent(playlist -> playlist.setName(name));
    }

    @PutMapping("/{id}/image")
    @Transactional
    public void updateImage(@PathVariable long id, @RequestPart MultipartFile image) {
        var playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));
        if (playlist.getImage() != null) {
            fileStore.unassociate(playlist.getImage());
            imageRepository.delete(playlist.getImage());
        }

        final var imageFile = new ImageFile();
        try {
            imageFile.setContent(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageFile.setName(image.getName());
        imageRepository.save(imageFile);
        fileStore.setContent(imageFile, imageFile.getContent());

        playlist.setImage(imageFile);
    }


    @PutMapping("/{id}/tracks")
    @Transactional
    public void addTracks(@PathVariable long id, @RequestBody Set<Long> trackIds) {
        var playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));
        final var tracks = trackRepository.findAllById(trackIds);
        playlist.getTracks().addAll(tracks);

        playlistRepository.save(playlist);
    }

    @DeleteMapping("/{id}/tracks")
    @Transactional
    public void deleteTracks(@PathVariable long id, @RequestBody Set<Long> trackIds) {
        var playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));
        CollectionUtils.filter(playlist.getTracks(), track -> !trackIds.contains(track.getId()));
    }
}
