package com.mytones.core.service;

import com.mytones.core.domain.file.ImageFile;
import com.mytones.core.repository.file.ImageRepository;
import com.mytones.core.repository.player.AlbumRepository;
import com.mytones.core.repository.player.TrackRepository;
import com.mytones.core.storage.FileStore;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final ImageRepository imageRepository;
    private final FileStore fileStore;

    public void deleteTrack(Long albumId, Long trackId) {
        final var album = albumRepository.findById(albumId).orElseThrow(NotFoundException::new);
        final var track = trackRepository.findById(trackId).orElseThrow(NotFoundException::new);
        album.removeTrack(track);
        fileStore.unassociate(track);
    }

    public void delete(Long albumId) {
        final var album = albumRepository.findById(albumId).orElseThrow(NotFoundException::new);
        album.getTracks().forEach(fileStore::unassociate);
        fileStore.unassociate(album.getImage());
        albumRepository.delete(album);
    }

    public ImageFile setImage(Long albumId, MultipartFile image) {
        final var album = albumRepository.findById(albumId).orElseThrow(NotFoundException::new);
        if (album.getImage() != null) {
            fileStore.unassociate(album.getImage());
            imageRepository.delete(album.getImage());
        }

        final var imageFile = new ImageFile();
        try {
            imageFile.setContent(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageFile.setName(image.getName());
        imageRepository.save(imageFile);
        fileStore.setContent(imageFile, imageFile.getContent());

        album.setImage(imageFile);

        return imageFile;
    }
}
