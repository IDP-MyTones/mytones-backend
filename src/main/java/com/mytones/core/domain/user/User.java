package com.mytones.core.domain.user;

import com.mytones.core.domain.file.ImageFile;
import com.mytones.core.domain.player.Playlist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User extends AbstractPersistable<Long> {

    @NaturalId
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username; // email address

    @Column(name = "PASSWORD", length = 1000, nullable = false)
    private String password; // hash

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private LocalDate birthday;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role = Role.CLIENT;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "IMAGE_ID")
    private ImageFile image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Playlist> playlists = new ArrayList<>();

    public void addPlaylist(Playlist playlist) {
       playlist.setUser(this);
       playlists.add(playlist);
    }

    public void removePlaylist(Playlist playlist) {
        CollectionUtils.filter(playlists, p -> !Objects.equals(p, playlist));
        playlist.setUser(null);
    }

    public void removeAllPlaylists() {
        playlists.forEach(p -> p.setUser(null));
        playlists.clear();
    }
}
