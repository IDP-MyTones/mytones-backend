package com.mytones.core.domain.file;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.InputStream;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class File extends AbstractPersistable<Long> {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String url;

    public abstract String bucket();

}
