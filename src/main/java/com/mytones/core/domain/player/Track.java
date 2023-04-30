package com.mytones.core.domain.player;

import com.mytones.core.domain.file.File;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRACKS")
@Getter
@Setter
public class Track extends File {

    @ManyToMany
    @OrderColumn(name = "ARTIST_ORDER")
    @JoinTable(
            name = "TRACK_ARTISTS",
            joinColumns = @JoinColumn(name = "TRACK_ID"),
            inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"),
            indexes = {
                    @Index(name = "IDX_TRACK_ARTISTS_ARTIST_ID", columnList = "ARTIST_ID"),
                    @Index(name = "IDX_TRACK_ARTISTS_TRACK_ID", columnList = "TRACK_ID")
            }
    )
    private Set<Artist> artists = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Album album;

    public void addArtist(Artist artistProfile) {
        artists.add(artistProfile);
    }
}
