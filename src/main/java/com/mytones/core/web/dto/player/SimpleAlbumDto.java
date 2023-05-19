package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Album;
import com.mytones.core.domain.player.Artist;
import com.mytones.core.web.dto.artist.SimpleArtistDto;
import com.mytones.core.web.dto.file.FileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = {"id", "image"}, allowGetters = true)
@NoArgsConstructor
public class SimpleAlbumDto {
    private Long id;
    private String name;
    private List<SimpleArtistDto> artists;
    private String imageUrl;

    public SimpleAlbumDto(Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.artists = album.getArtists().stream().map(SimpleArtistDto::new).toList();
        this.imageUrl = album.getImageUrl();
    }
}
