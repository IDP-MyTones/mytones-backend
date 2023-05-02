package com.mytones.core.web.controller.player;

import com.mytones.core.domain.player.Album;
import com.mytones.core.domain.player.Track;
import com.mytones.core.repository.player.AlbumRepository;
import com.mytones.core.repository.player.ArtistRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.service.AlbumService;
import com.mytones.core.storage.FileStore;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.artist.SimpleArtistDto;
import com.mytones.core.web.dto.file.FileDto;
import com.mytones.core.web.dto.player.SimpleAlbumDto;
import com.mytones.core.web.dto.player.SimpleTrackDto;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constants.BASE_URL + "/albums")
@RequiredArgsConstructor
class AlbumController {

    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistProfileRepository;
    private final TrackRepository trackRepository;
    private final FileStore fileStore;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public SimpleAlbumDto create(@RequestBody SimpleAlbumDto albumDto) {
        final var album = new Album();
        album.setName(albumDto.getName());
        album.setReleaseDate(albumDto.getReleaseDate());
        album.setType(albumDto.getType());

        albumRepository.save(album);
        albumDto.setId(album.getId());

        return albumDto;
    }

    @PostMapping(value = "/{id}/tracks")
    @Transactional
    public SimpleTrackDto addTrack(@PathVariable long id, @RequestBody SimpleTrackDto data) {
        final var track = new Track();
        track.setName(data.getName());

        data.getArtists().stream().map(artistProfileRepository::findById)
                .flatMap(Optional::stream)
                .forEach(track::addArtist);

        trackRepository.save(track);

        final var album = albumRepository.findById(id).orElseThrow(NotFoundException::new);
        track.setAlbum(album);
        album.getTracks().add(track);

        data.setId(track.getId());
        return data;
    }

    @PostMapping(value = "/{id}/tracks/{trackId}/upload")
    @Transactional
    public void addTrack(@PathVariable long id, @PathVariable long trackId, @RequestPart MultipartFile audio) throws IOException {
        var track = trackRepository.findById(trackId).filter(t -> t.getAlbum().getId().equals(id)).orElseThrow(NotFoundException::new);
        fileStore.setContent(track, audio.getInputStream());
    }

    @DeleteMapping("/{albumId}/tracks/{trackId}")
    public void deleteTrack(@PathVariable long albumId, @PathVariable long trackId) {
        albumService.deleteTrack(albumId, trackId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        albumService.delete(id);
    }

    @PutMapping("/{id}/image")
    public FileDto updateImage(@PathVariable long id, @RequestPart MultipartFile image) {
        var imageFile = albumService.setImage(id, image);
        return modelMapper.map(imageFile, FileDto.class);
    }

    @PutMapping("/{id}")
    @Transactional
    public SimpleAlbumDto update(@PathVariable long id, @RequestBody SimpleAlbumDto albumDto) {
        final var album = albumRepository.findById(id).orElseThrow(NotFoundException::new);
        album.setName(albumDto.getName());
        album.setReleaseDate(albumDto.getReleaseDate());
        album.setType(albumDto.getType());

        albumRepository.save(album);
        albumDto.setId(album.getId());

        albumDto.setId(id);
        return albumDto;
    }

    @GetMapping("/search")
    @Secured({"CLIENT", "MODERATOR", "ADMIN"})
    public List<SimpleAlbumDto> search(@RequestParam String value) {
        return albumRepository.findAllByNameLikeIgnoreCase("%" + value + "%").stream()
                .map(SimpleAlbumDto::new)
                .toList();
    }
}
