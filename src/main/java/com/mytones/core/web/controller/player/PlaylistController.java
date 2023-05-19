package com.mytones.core.web.controller.player;

import com.mytones.core.domain.player.Playlist;
import com.mytones.core.repository.player.PlaylistRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.security.CurrentUserHolder;
import com.mytones.core.storage.StorageUtils;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.player.PlaylistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.BASE_URL + "/playlists")
@RequiredArgsConstructor
class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final CurrentUserHolder currentUserHolder;
    private final StorageUtils storageUtils;

    @GetMapping("/{id}")
    @Transactional
    public PlaylistDto get(@PathVariable long id) {
        var playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));

        if (Constants.FAVORITES_PLAYLIST_KEY.equals(playlist.getName()) || !playlist.getUser().equals(currentUserHolder.get())) {
            throw new NotFoundException();
        }

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

        if (Constants.FAVORITES_PLAYLIST_KEY.equals(playlist.getName()) || !playlist.getUser().equals(currentUserHolder.get())) {
            throw new NotFoundException();
        }

        currentUserHolder.get().removePlaylist(playlist);
    }

    @PutMapping("/{id}")
    @Transactional
    public void update(@PathVariable long id, @RequestBody String name) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(NotFoundException::new);
        if (Constants.FAVORITES_PLAYLIST_KEY.equals(playlist.getName()) || !playlist.getUser().equals(currentUserHolder.get())) {
            throw new NotFoundException();
        }

        playlist.setName(name);
        playlistRepository.save(playlist);
    }

    @PutMapping("/{id}/image")
    @Transactional
    public void updateImage(@PathVariable long id, @RequestBody String content) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(NotFoundException::new);
        if (Constants.FAVORITES_PLAYLIST_KEY.equals(playlist.getName()) || !playlist.getUser().equals(currentUserHolder.get())) {
            throw new NotFoundException();
        }

        storageUtils.uploadPlaylistImage(playlist, content);
        playlistRepository.save(playlist);
    }


    @PostMapping("/{playlistId}/tracks/{trackId}")
    @Transactional
    public void addTrack(@PathVariable long playlistId, @PathVariable long trackId) {
        final var playlist = playlistRepository.findById(playlistId).orElseThrow(NotFoundException::new);
        if (Constants.FAVORITES_PLAYLIST_KEY.equals(playlist.getName()) || !playlist.getUser().equals(currentUserHolder.get())) {
            throw new NotFoundException();
        }

        final var track = trackRepository.findById(trackId).orElseThrow(NotFoundException::new);
        playlist.getTracks().add(track);

        playlistRepository.save(playlist);
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    @Transactional
    public void deleteTracks(@PathVariable long playlistId, @PathVariable long trackId) {
        final var playlist = playlistRepository.findById(playlistId).orElseThrow(NotFoundException::new);
        if (Constants.FAVORITES_PLAYLIST_KEY.equals(playlist.getName()) || !playlist.getUser().equals(currentUserHolder.get())) {
            throw new NotFoundException();
        }

        playlist.getTracks().removeIf(t -> Objects.equals(t.getId(), trackId));

        playlistRepository.save(playlist);
    }

    @GetMapping("/search")
    public List<PlaylistDto> search(@RequestParam String value) {
        return playlistRepository.findAllByNameLikeAndUserId("%" + value + "%", currentUserHolder.get().getId())
                .stream().map(PlaylistDto::new)
                .toList();
    }

    @PostMapping("/favorites/{trackId}")
    @Transactional
    public void addToFavorites(@PathVariable Long trackId) {
        var playlist = playlistRepository.findAllByNameLikeAndUserId( Constants.FAVORITES_PLAYLIST_KEY, currentUserHolder.get().getId()).get(0);
        var track = trackRepository.findById(trackId).orElseThrow(NotFoundException::new);

        playlist.getTracks().add(track);
        playlistRepository.save(playlist);
    }


    @DeleteMapping("/favorites/{trackId}")
    @Transactional
    public void removeFromFavorites(@PathVariable Long trackId) {
        var playlist = playlistRepository.findAllByNameLikeAndUserId( Constants.FAVORITES_PLAYLIST_KEY, currentUserHolder.get().getId()).get(0);

        playlist.setTracks(
                playlist.getTracks().stream()
                        .filter(track -> !Objects.equals(track.getId(), trackId))
                        .collect(Collectors.toList())
        );

        playlistRepository.save(playlist);
    }
}
