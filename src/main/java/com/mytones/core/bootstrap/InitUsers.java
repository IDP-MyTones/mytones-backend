package com.mytones.core.bootstrap;

import com.mytones.core.domain.user.Role;
import com.mytones.core.repository.user.UserRepository;
import com.mytones.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class InitUsers implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            var user = userService.createUser("admin", "admin", "Thanos", "admin");

            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}
