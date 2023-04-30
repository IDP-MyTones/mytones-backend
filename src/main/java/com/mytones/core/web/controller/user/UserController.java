package com.mytones.core.web.controller.user;

import com.mytones.core.domain.file.ImageFile;
import com.mytones.core.repository.file.ImageRepository;
import com.mytones.core.security.CurrentUserHolder;
import com.mytones.core.storage.FileStore;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(Constants.BASE_URL + "/users")
@RequiredArgsConstructor
class UserController {

    private final ImageRepository imageRepository;
    private final CurrentUserHolder currentUserHolder;
    private final FileStore fileStore;

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UserDto getMyProfile() {
        return new UserDto(currentUserHolder.getProfile());
    }

    @PutMapping("/me/image")
    @Transactional
    public void updateImage(@RequestPart MultipartFile image) {
        var user = currentUserHolder.get();
        if (user.getImage() != null) {
            fileStore.unassociate(user.getImage());
            imageRepository.delete(user.getImage());
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

        user.setImage(imageFile);
    }
}
