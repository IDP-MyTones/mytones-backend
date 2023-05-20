package com.mytones.core.web.controller.admin;

import com.mytones.core.repository.user.UserRepository;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.user.SimpleUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/admin")
@RequiredArgsConstructor
@Secured("ADMIN")
class AdminController {

    public final UserRepository userRepository;

    @GetMapping("/users")
    public List<SimpleUserDto> findAllUsers() {
        return userRepository.findAll().stream().map(SimpleUserDto::new).toList();
    }
}
