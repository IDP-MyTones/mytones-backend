package com.mytones.core.web.dto.artist;

import com.mytones.core.domain.player.Artist;
import com.mytones.core.web.dto.player.SimpleAlbumDto;

import java.util.List;

public record FullArtistDto(Long id, String name, String description, String imageUrl, List<SimpleAlbumDto> albums) {

    public FullArtistDto(Artist artist) {
        this(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                artist.getImageUrl(),
                artist.getAlbums().stream().map(SimpleAlbumDto::new).toList()
        );
    }
}
