package com.mytones.core.web.controller.player;

import com.mytones.core.repository.player.PlaylistRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.security.CurrentUserHolder;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.player.SimpleTrackDto;
import com.mytones.core.web.dto.player.TrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackRepository trackRepository;
    private final CurrentUserHolder currentUserHolder;
    private final PlaylistRepository playlistRepository;

    @GetMapping("/search")
    @Secured({"CLIENT", "MODERATOR", "ADMIN"})
    @Transactional
    public List<TrackDto> search(@RequestParam String value) {
        return trackRepository.findAllByNameLikeIgnoreCase("%" + value + "%").stream()
                .map(TrackDto::new)
                .toList();
    }

    @GetMapping("/albums/{albumId}")
    @Transactional(readOnly = true)
    public Page<TrackDto> albumTracks(@PathVariable long albumId, @PageableDefault(sort = "id") Pageable pageable) {
        return trackRepository.findAllByAlbumId(albumId, pageable)
                .map(TrackDto::new)
                .map(this::decorateWithFavorites);
    }

    @GetMapping("/playlists/{albumId}")
    @Transactional(readOnly = true)
    public Page<TrackDto> playlistsTracks(@PathVariable long albumId, @PageableDefault(sort = "id") Pageable pageable) {
        return trackRepository.findAllByPlaylistIdAndPlaylistUserId(albumId, currentUserHolder.get().getId(), pageable)
                .map(TrackDto::new)
                .map(this::decorateWithFavorites);
    }

    private TrackDto decorateWithFavorites(TrackDto trackDto) {
        var track = playlistRepository.findTrackInFavorites(currentUserHolder.get().getId(), trackDto.getId());

        trackDto.setInFavorites(track.isPresent());
        return trackDto;
    }
}
