package com.mytones.core.web.dto.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytones.core.domain.player.Album;
import com.mytones.core.web.dto.artist.SimpleArtistDto;
import com.mytones.core.web.dto.file.FileDto;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(value = {"id", "image", "tracks"}, allowGetters = true)
public record AlbumDto(Long id,
                       String name,
                       LocalDate releaseDate,
                       FileDto image,
                       List<SimpleArtistDto> artists,
                       List<TrackDto> tracks) {
}
