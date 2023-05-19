package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Playlist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(value = "id", allowGetters = true)
@NoArgsConstructor
public class PlaylistDto {

    private Long id;
    private String name;
    private String imageUrl;

    public PlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.imageUrl = playlist.getImageUrl();
    }
}
