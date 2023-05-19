package com.mytones.core.web.controller.user;

import com.mytones.core.security.CurrentUserHolder;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.BASE_URL + "/users")
@RequiredArgsConstructor
class UserController {

    private final CurrentUserHolder currentUserHolder;

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UserDto getMyProfile() {
        return new UserDto(currentUserHolder.getProfile());
    }


}
