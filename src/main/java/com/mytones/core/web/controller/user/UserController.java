package com.mytones.core.web.controller.user;

import com.mytones.core.repository.user.UserRepository;
import com.mytones.core.security.CurrentUserHolder;
import com.mytones.core.service.UserService;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.user.SingUpDto;
import com.mytones.core.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.BASE_URL + "/users")
@RequiredArgsConstructor
class UserController {

    private final CurrentUserHolder currentUserHolder;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UserDto getMyProfile() {
        return new UserDto(currentUserHolder.getProfile());
    }

    @PostMapping
    public UserDto addUser(@RequestBody SingUpDto singUpDto) {
        if (userRepository.findByUsername(singUpDto.username()).isPresent()) {
            throw new AccessDeniedException("Username is used");
        }

        final var user = userService.createUser(singUpDto.username(), singUpDto.firstName(), singUpDto.lastName());

        return new UserDto(user);
    }

}
