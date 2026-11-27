package org.example.cocktails.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    private final AuthenticationManager users;
    private final JwtEncoder encoder;

    public TokenService(AuthenticationManager users, JwtEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public String generateToken(String username, String password) {
        var authenticated = users.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password));
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authenticated.getName())
                .claim("scope", authenticated.getAuthorities().stream()
                        .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                        .map(authority -> authority.getAuthority().replace("ROLE_", "")).toList())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
