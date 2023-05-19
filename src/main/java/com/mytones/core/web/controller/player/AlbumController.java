package com.mytones.core.web.controller.player;

import com.mytones.core.domain.player.Album;
import com.mytones.core.domain.player.Track;
import com.mytones.core.repository.player.AlbumRepository;
import com.mytones.core.repository.player.ArtistRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.service.AlbumService;
import com.mytones.core.storage.StorageUtils;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.player.CreateAlbumDto;
import com.mytones.core.web.dto.player.CreateTrackDto;
import com.mytones.core.web.dto.player.SimpleAlbumDto;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/albums")
@RequiredArgsConstructor
class AlbumController {

    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final StorageUtils storageUtils;
    private final ArtistRepository artistProfileRepository;
    private final TrackRepository trackRepository;


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public SimpleAlbumDto getById(@PathVariable long id) {
        var album = albumRepository.findById(id).orElseThrow(NotFoundException::new);

        return new SimpleAlbumDto(album);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public SimpleAlbumDto create(@RequestBody CreateAlbumDto albumDto) {
        final var album = new Album();
        album.setName(albumDto.name());
        albumRepository.save(album);
        storageUtils.uploadAlbumImage(album, albumDto.imageContent());
        albumDto.tracks().forEach(dto -> uploadTrack(dto, album));

        albumDto.artistIds().forEach(artistId -> {
            final var artist = artistProfileRepository.findById(artistId).orElseThrow(NotFoundException::new);
            artist.getAlbums().add(album);
            album.getArtists().add(artist);
        });
        albumRepository.save(album);

        return new SimpleAlbumDto(album);
    }

    private void uploadTrack(CreateTrackDto trackDto, Album album) {
        final var track = new Track();
        trackDto.artistIds().forEach(artistId -> track.addArtist(artistProfileRepository.getReferenceById(artistId)));
        album.getTracks().add(track);
        track.setAlbum(album);
        track.setName(trackDto.name());
        track.setDuration(trackDto.duration());

        trackRepository.save(track);
        storageUtils.uploadTrack(track, trackDto.content());
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        albumService.delete(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public SimpleAlbumDto update(@PathVariable long id, @RequestBody SimpleAlbumDto albumDto) {
        final var album = albumRepository.findById(id).orElseThrow(NotFoundException::new);
        album.setName(albumDto.getName());

        albumRepository.save(album);
        albumDto.setId(album.getId());

        albumDto.setId(id);
        return albumDto;
    }

    @GetMapping("/search")
    @Secured({"CLIENT", "MODERATOR", "ADMIN"})
    @Transactional(readOnly = true)
    public List<SimpleAlbumDto> search(@RequestParam String value) {
        return albumRepository.findAllByNameLikeIgnoreCase("%" + value + "%").stream()
                .map(SimpleAlbumDto::new)
                .toList();
    }
}
