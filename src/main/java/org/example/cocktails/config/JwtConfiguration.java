package org.example.cocktails.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfiguration {
    // Schulungssecret aus SocialDrinks. Produktiv über Umgebungsvariablen oder
    // einen Secret Manager injizieren, statt es im Quellcode abzulegen.
    private static final String SECRET = "s/4KMb61LOrMYYAn4rfaQYSgr+le5SMrsMzKw8G6bXc=";
    // Wie in SocialDrinks: UTF-8-Bytes des Strings, keine Base64-Decodierung.
    private final SecretKey key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new OctetSequenceKey.Builder(key).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
