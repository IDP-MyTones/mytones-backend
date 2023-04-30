package com.mytones.core.security;

import com.mytones.core.domain.user.Role;
import com.mytones.core.domain.user.User;
import com.mytones.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserHolder {

    private final UserRepository userRepository;

    public String getUsername() {
        verifyAuthentication();

        return SecurityUtils.getCurrentUserLogin().get();
    }

    public User get() {
        final String username = getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found username"));
    }

    public User getProfile() {
        final String username = getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found username"));
    }

    public boolean isAdmin() {
        return SecurityUtils.isCurrentUserInRole(Role.ADMIN.name());
    }

    private void verifyAuthentication() {
        if (!SecurityUtils.isAuthenticated()) {
            throw new AccessDeniedException("Access is denied");
        }
    }

}
