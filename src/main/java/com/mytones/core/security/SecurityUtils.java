package com.mytones.core.security;

import com.mytones.core.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

public class SecurityUtils {

    public static Optional<String> getCurrentUserLogin() {
        var securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof final UserDetails springSecurityUser) {
                        return springSecurityUser.getUsername();
                    }
                    if (authentication.getPrincipal() instanceof final String authenticationPrincipal) {
                        return authenticationPrincipal;
                    }
                    if (authentication.getPrincipal() instanceof final User user) {
                        return user.getUsername();
                    }
                    if (authentication.getPrincipal() instanceof final TokenValidator.UserDto user) {
                        return user.getUsername();
                    }
                    return null;
                });
    }

    public static boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean isCurrentUserInRole(final String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).anyMatch(role::equalsIgnoreCase);
    }

    private static Stream<String> getAuthorities(final Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }
}
