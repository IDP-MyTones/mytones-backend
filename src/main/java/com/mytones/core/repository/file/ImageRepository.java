package com.mytones.core.repository.file;

import com.mytones.core.domain.file.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageFile, Long> {
}
