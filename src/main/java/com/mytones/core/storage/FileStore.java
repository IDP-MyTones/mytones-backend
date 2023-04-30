package com.mytones.core.storage;

import com.mytones.core.domain.file.File;
import org.springframework.content.commons.store.ContentStore;

public interface FileStore extends ContentStore<File, String> {
}
