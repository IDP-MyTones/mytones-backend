package com.mytones.core.domain.player;

import com.mytones.core.domain.file.ImageFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ALBUMS")
@Getter
@Setter
public class Album extends AbstractPersistable<Long> {

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "RELEASE_DATE")
    @Temporal(TemporalType.DATE)
    private LocalDate releaseDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "IMAGE_ID")
    private ImageFile image;

    @ManyToMany
    @OrderColumn(name = "ARTIST_INDEX")
    @JoinTable(
            name = "ALBUM_ARTISTS",
            joinColumns = @JoinColumn(name = "ALBUM_ID"),
            inverseJoinColumns = @JoinColumn(name = "ARTIST_ID")
    )
    private List<Artist> artists = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> tracks = new ArrayList<>();

    public enum Type {
        SINGLE, EP, ALBUM
    }

    public void removeTrack(Track track) {
        if (track.getAlbum() == this) {
            CollectionUtils.filter(tracks, t -> !Objects.equals(t, track));
            track.setAlbum(null);
        }
    }
}
