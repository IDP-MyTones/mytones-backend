package com.mytones.core.domain.file;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "IMAGES")
@Getter
@Setter
public class ImageFile extends File {
}
