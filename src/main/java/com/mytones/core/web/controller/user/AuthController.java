package com.mytones.core.web.controller.user;

import com.mytones.core.domain.user.User;
import com.mytones.core.repository.user.UserRepository;
import com.mytones.core.security.SecurityUtils;
import com.mytones.core.utils.Constants;
import com.mytones.core.web.dto.user.LoginDto;
import com.mytones.core.web.dto.user.SingUpDto;
import com.mytones.core.web.dto.user.TokenDto;
import com.mytones.core.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping(Constants.BASE_URL + "/auth")
@RequiredArgsConstructor
class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto) {
        var user = userRepository.findByUsername(loginDto.username()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Bad credentials");
        }

        return new TokenDto(SecurityUtils.generateToken(user));
    }

    @PostMapping("/singup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto singUp(@RequestBody SingUpDto singUpDto) {
        if (userRepository.findByUsername(singUpDto.username()).isPresent()) {
            throw new AccessDeniedException("Username is used");
        }

        final var user = new User();
        user.setBirthday(singUpDto.birthday());
        user.setFirstName(singUpDto.firstName());
        user.setLastName(singUpDto.lastName());
        user.setUsername(singUpDto.username());
        user.setPassword(passwordEncoder.encode(singUpDto.password()));

        userRepository.save(user);

        return new UserDto(user);
    }
}
