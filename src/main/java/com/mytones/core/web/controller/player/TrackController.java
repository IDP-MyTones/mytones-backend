package com.mytones.core.web.controller.player;

import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.web.dto.player.SimpleTrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackRepository trackRepository;

    @GetMapping("/search")
    @Secured({"CLIENT", "MODERATOR", "ADMIN"})
    public List<SimpleTrackDto> search(@RequestParam String value) {
        return trackRepository.findAllByNameLikeIgnoreCase("%" + value + "%").stream()
                .map(SimpleTrackDto::new)
                .toList();
    }
}
