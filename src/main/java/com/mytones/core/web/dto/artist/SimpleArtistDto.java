package com.mytones.core.web.dto.artist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Artist;
import jakarta.annotation.Nonnull;


@JsonIgnoreProperties(value = {"id", "imageId"}, allowGetters = true)
public record SimpleArtistDto(Long id, @Nonnull String name, @Nonnull String description, Long imageId) {

    public SimpleArtistDto(Artist artist) {
        this(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                null
//                artist.getImage() == null ? null : artist.getImage().getId()
        );
    }
}
