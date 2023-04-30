package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Album;
import com.mytones.core.domain.player.Artist;
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
    private Album.Type type;
    private String name;
    private LocalDate releaseDate;
    private List<Long> artists;
    private FileDto image;

    public SimpleAlbumDto(Album album) {
        this.id = album.getId();
        this.type = album.getType();
        this.name = album.getName();
        this.releaseDate = album.getReleaseDate();
        this.artists = album.getArtists().stream().map(Artist::getId).toList();

        if (album.getImage() != null) {
            image = new FileDto();
            image.setId(album.getImage().getId());
        }
    }
}
