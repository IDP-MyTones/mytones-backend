package com.mytones.core.web.controller.artist;

import com.mytones.core.domain.file.ImageFile;
import com.mytones.core.domain.player.Artist;
import com.mytones.core.repository.file.ImageRepository;
import com.mytones.core.repository.player.ArtistRepository;
import com.mytones.core.storage.FileStore;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.artist.SimpleArtistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/artists")
@RequiredArgsConstructor
@Secured({"ADMIN", "MODERATOR"})
class ArtistController {

    private final ArtistRepository artistRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;

    @PostMapping
    @Transactional
    public SimpleArtistDto create(@Validated @RequestBody SimpleArtistDto artistDto) {
        var artist = new Artist();

        artist.setName(artistDto.name());
        artist.setDescription(artistDto.description());

        artistRepository.save(artist);
        return new SimpleArtistDto(artist);
    }

    @PutMapping("/{id}")
    @Transactional
    public SimpleArtistDto update(@PathVariable long id, @RequestBody SimpleArtistDto artistDto) {
        var artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundException("Artist not found"));

        artist.setName(artistDto.name());
        artist.setDescription(artistDto.description());

        artistRepository.save(artist);

        return new SimpleArtistDto(artist);
    }

    @PutMapping("/{id}/image")
    @Transactional
    public SimpleArtistDto updateImage(@PathVariable long id, @RequestPart MultipartFile image) {
        var artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundException("Artist not found"));
        if (artist.getImage() != null) {
            fileStore.unassociate(artist.getImage());
        }

        final var imageFile = new ImageFile();
        try {
            imageFile.setContent(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file", e);
        }

        imageFile.setName(image.getName());
        imageRepository.save(imageFile);
        fileStore.setContent(imageFile, imageFile.getContent());

        artist.setImage(imageFile);

        return new SimpleArtistDto(artist);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable long id) {
        var artist = artistRepository.findById(id).orElseThrow(() -> new NotFoundException("Artist not found"));
        if (artist.getImage() != null) {
            fileStore.unassociate(artist.getImage());
        }
        artistRepository.delete(artist);
    }

    @GetMapping("/search")
    @Secured({"CLIENT", "MODERATOR", "ADMIN"})
    public List<SimpleArtistDto> search(@RequestParam String value) {
        return artistRepository.findAllByNameLikeIgnoreCase("%" + value + "%").stream()
                .map(SimpleArtistDto::new)
                .toList();
    }
}
