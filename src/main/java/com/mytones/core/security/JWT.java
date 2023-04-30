package com.mytones.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;


class JWT {
    private static final String SECRET = "645267556B58703273357638792F413F4428472B4B6250655368566D597133743677397A244326452948404D635166546A576E5A7234753778214125442A472D";

    private final SecretKey secretKey;
    private final String username;
    private final String role;

    JWT(String token) {
        var bytes = Decoders.BASE64.decode(SECRET);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        var claims = extractClaims(token);

        if (claims.getExpiration().before(new Date())) {
            throw new CredentialsExpiredException("Access token is expired");
        }

        this.username = claims.getSubject();
        this.role = claims.get("role", String.class);
    }

    JWT(UserDetails userDetails) {
        var bytes = Decoders.BASE64.decode(SECRET);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        this.username = userDetails.getUsername();
        this.role = IterableUtils.first(userDetails.getAuthorities()).getAuthority();
    }


    public String compact() {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setExpiration(DateUtils.addDays(new Date(), 1))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
