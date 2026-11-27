package org.example.cocktails.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnWebApplication
public class SecurityConfiguration {
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        return http.securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/cocktails/**", "/api/ingredients/**").permitAll()
                        .requestMatchers("/api/fridge/**").hasAuthority("SCOPE_USER")
                        .anyRequest().authenticated())
                // Nur diese API-Kette verwendet ausschließlich explizite Bearer-Header.
                // MVC und Security Lab behalten ihren eigenen Cookie-/CSRF-Schutz.
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcSecurity(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**", "/mvc/user/**").hasRole("USER")
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.spa())
                .formLogin(login -> login.defaultSuccessUrl("/mvc", false))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                .build();
    }
}
