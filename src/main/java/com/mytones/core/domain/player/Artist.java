package com.mytones.core.domain.player;

import com.mytones.core.domain.file.ImageFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ARTISTS")
@Getter
@Setter
public class Artist extends AbstractPersistable<Long> {

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "IMAGE_ID")
    private ImageFile image;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "ARTIST_ALBUMS",
            joinColumns = @JoinColumn(name = "ARTIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "ALBUM_ID")
    )
    private List<Album> albums = new ArrayList<>();
}
