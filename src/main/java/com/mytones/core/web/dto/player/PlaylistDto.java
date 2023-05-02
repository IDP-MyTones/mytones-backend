package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Playlist;
import com.mytones.core.web.dto.file.FileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = "id", allowGetters = true)
@NoArgsConstructor
public class PlaylistDto {

    private Long id;
    private String name;
    private FileDto image;
    private List<TrackDto> tracks = new ArrayList<>();

    public PlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        if (playlist.getImage() != null) {
            this.image = new FileDto();
            this.image.setId(playlist.getImage().getId());
        }

        playlist.getTracks().stream().map(TrackDto::new).forEach(tracks::add);
    }
}
