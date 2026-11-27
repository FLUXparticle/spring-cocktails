package org.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfiguration {
    // Demo-Passwort aus SocialDrinks. Produktiv Benutzer und Passwort-Hashes aus
    // einer Benutzerverwaltung laden; Start-Secrets über Konfiguration/Secret Manager.
    private static final String DEMO_PASSWORD = "Secret123";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("employee").password(encoder.encode(DEMO_PASSWORD)).roles("USER").build(),
                User.withUsername("admin").password(encoder.encode(DEMO_PASSWORD)).roles("USER", "ADMIN").build());
    }

}
