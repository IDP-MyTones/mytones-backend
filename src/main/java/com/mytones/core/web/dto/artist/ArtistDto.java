package com.mytones.core.web.dto.artist;

import com.mytones.core.domain.player.Artist;

public record ArtistDto(Long id, String name, String description, String imageUrl) {

    public ArtistDto(Artist artist) {
        this(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                artist.getImageUrl()
        );
    }

}
