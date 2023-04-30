package com.mytones.core.bootstrap;

import com.mytones.core.domain.user.Role;
import com.mytones.core.domain.user.User;
import com.mytones.core.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class InitUsers implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            var user = new User();
            user.setRole(Role.ADMIN);
            user.setPassword(passwordEncoder.encode("admin"));
            user.setUsername("admin");
            user.setLastName("Thanos");
            user.setFirstName("Admin");

            userRepository.save(user);
        }
    }
}
