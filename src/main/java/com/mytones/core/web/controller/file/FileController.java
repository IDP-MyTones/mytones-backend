package com.mytones.core.web.controller.file;

import com.mytones.core.domain.file.File;
import com.mytones.core.repository.file.FileRepository;
import com.mytones.core.storage.FileStore;
import com.mytones.core.utils.Constants;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = Constants.BASE_URL + "/files")
@AllArgsConstructor
class FileController {

    private final FileRepository fileRepository;
    private final FileStore fileStore;

    @GetMapping(value = "/{id}/download")
    public void download(@PathVariable long id, HttpServletResponse response) {
        try {
            File file = fileRepository.findById(id).orElseThrow(NotFoundException::new);
            InputStream content = fileStore.getContent(file);
            IOUtils.copy(content, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
