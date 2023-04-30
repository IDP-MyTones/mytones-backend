package com.mytones.core.domain.player;

import com.mytones.core.domain.file.ImageFile;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_ID")
    private ImageFile image;

    @ManyToMany
    @OrderColumn(name = "ARTIST_INDEX")
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
