package com.mytones.core.web.dto.player;

import java.util.List;

public record CreateTrackDto(String name, List<Long> artistIds, String content, Long duration) {
}
