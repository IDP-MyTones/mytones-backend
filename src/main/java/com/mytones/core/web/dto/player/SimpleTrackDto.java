package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Artist;
import com.mytones.core.domain.player.Track;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = "id", allowGetters = true)
@NoArgsConstructor
public class SimpleTrackDto {

    private Long id;
    private String name;
    private List<Long> artists;

    public SimpleTrackDto(Track track) {
        this.id = track.getId();
        this.name = track.getName();
        this.artists = track.getArtists().stream().map(Artist::getId).toList();
    }
}
