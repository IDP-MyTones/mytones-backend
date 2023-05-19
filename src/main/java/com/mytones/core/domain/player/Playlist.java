package com.mytones.core.domain.player;

import com.mytones.core.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYLISTS")
@Getter
@Setter
public class Playlist extends AbstractPersistable<Long> {

    @Column(name = "NAME")
    private String name;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @ManyToMany
    @OrderColumn(name = "TRACK_INDEX")
    @JoinTable(
            name = "PLAYLIST_TRACKS",
            joinColumns = @JoinColumn(name = "PLAYLIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRACK_ID")
    )
    private List<Track> tracks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
