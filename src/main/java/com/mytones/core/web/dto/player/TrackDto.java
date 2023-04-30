package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Track;
import com.mytones.core.web.dto.artist.SimpleArtistDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = "id", allowGetters = true)
public class TrackDto {

    private Long id;
    private String name;
    private List<SimpleArtistDto> artists;
    private Long albumId;

    public TrackDto(Track track) {
        this.id = track.getId();
        this.name = track.getName();
        this.artists = track.getArtists().stream().map(SimpleArtistDto::new).toList();
        this.albumId = track.getAlbum().getId();
    }
}
