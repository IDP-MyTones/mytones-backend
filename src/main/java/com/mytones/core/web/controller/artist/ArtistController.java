package com.mytones.core.web.controller.artist;

import com.mytones.core.domain.player.Artist;
import com.mytones.core.repository.player.AlbumRepository;
import com.mytones.core.repository.player.ArtistRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.storage.StorageUtils;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.artist.ArtistDto;
import com.mytones.core.web.dto.artist.CreateArtistDto;
import com.mytones.core.web.dto.artist.FullArtistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/artists")
@RequiredArgsConstructor
@Secured({"ADMIN", "MODERATOR"})
class ArtistController {

    private final ArtistRepository artistRepository;
    private final StorageUtils storageUtils;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    @PostMapping
    @Transactional
    public ArtistDto create(@Validated @RequestBody CreateArtistDto artistDto) {
        var artist = new Artist();

        artist.setName(artistDto.name());
        artist.setDescription(artistDto.description());

        artistRepository.save(artist);

        storageUtils.uploadArtistImage(artist, artistDto.imageContent());

        return new ArtistDto(artist);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public FullArtistDto getArtist(@PathVariable long id) {
        final var artist = artistRepository.findById(id).orElseThrow(NotFoundException::new);
        return new FullArtistDto(artist);
    }

    @PutMapping("/{id}")
    @Transactional
    public ArtistDto update(@PathVariable long id, @RequestBody ArtistDto artistDto) {
        var artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundException("Artist not found"));

        artist.setName(artistDto.name());
        artist.setDescription(artistDto.description());

        if (artistDto.imageUrl() != null && !artistDto.imageUrl().startsWith("http")) {
            storageUtils.uploadArtistImage(artist, artistDto.imageUrl());
        }

        artistRepository.save(artist);

        return new ArtistDto(artist);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable long id) {
        var artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundException("Artist not found"));
        artist.getAlbums().forEach(album -> {
            album.getArtists().remove(artist);
            albumRepository.save(album);
        });
        artist.setAlbums(new ArrayList<>());

        trackRepository.findAllByArtistsContains(artist)
                        .forEach(track ->  {
                            track.getArtists().remove(artist);
                            trackRepository.save(track);
                        });

        storageUtils.deleteArtistImage(artist);

        artistRepository.delete(artist);
    }

    @GetMapping("/search")
    @Secured({"CLIENT", "MODERATOR", "ADMIN"})
    @Transactional(readOnly = true)
    public List<ArtistDto> search(@RequestParam String value) {
        return artistRepository.findAllByNameLikeIgnoreCase("%" + value + "%").stream()
                .map(ArtistDto::new)
                .toList();
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Page<ArtistDto> listArtists(@PageableDefault(size = 20) Pageable pageable) {
        return artistRepository.findAll(pageable).map(ArtistDto::new);
    }
}
