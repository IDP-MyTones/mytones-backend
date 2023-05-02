package com.mytones.core.web.dto.user;


import java.time.LocalDate;

public record SingUpDto(String firstName,
                        String lastName,
                        String username,
                        String password,
                        LocalDate birthday) {
}
