package com.mytones.core.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
class BearerAuthHeader {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";

    private final String value;

    BearerAuthHeader(HttpServletRequest request) {
        final String header = request.getHeader(AUTHORIZATION_HEADER);
        this.value = StringUtils.startsWith(header, BEARER_TOKEN)
                ? StringUtils.substring(header, BEARER_TOKEN.length() - 1)
                : null;
    }

    public boolean exists() {
        return StringUtils.isNotEmpty(value);
    }
}
