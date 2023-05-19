package com.mytones.core.web.dto.player;

import java.util.List;

public record CreateAlbumDto(String name,
                             List<Long> artistIds,
                             String imageContent,
                             List<CreateTrackDto> tracks) {
}