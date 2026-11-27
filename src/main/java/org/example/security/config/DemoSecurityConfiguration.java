package org.example.security.config;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.web.cors.*;

import java.util.*;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(DemoSettings.class)
public class DemoSecurityConfiguration {
    @Bean
    SecurityFilterChain securityDemoChain(HttpSecurity http, DemoSettings settings) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/security-demo/notes/all").hasRole("ADMIN")
                        .requestMatchers("/security-demo/notes/me").hasRole("USER")
                        .anyRequest().permitAll())
                .formLogin(login -> login.defaultSuccessUrl("/good-user.html", false))
                .logout(logout -> logout.logoutSuccessUrl("/good-user.html"))
                .exceptionHandling(errors -> errors.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();
                    // Schulungsfall: Eine pauschale Freigabe aus der Entwicklung
                    // ist versehentlich auf dem Live-Server gelandet, inklusive Cookies.
                    config.setAllowedOriginPatterns(settings.isAllowAnyOrigin() ? List.of("*") : List.of());
                    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
                    config.setAllowCredentials(true);
                    return config;
                }));
        if (settings.isCsrfEnabled()) {
            // SOLUTION for cookie/session authentication. AngularJS convention:
            // cookie XSRF-TOKEN -> header X-XSRF-TOKEN (not X-CSRF-TOKEN).
            http.csrf(csrf -> csrf.spa());
        } else {
            // DELIBERATELY UNSAFE, only inside the explicit security-demo profile
            // of the separate SecurityApplication. Never copy this dev setting to production.
            // Stateless bearer-only APIs are a different authentication model;
            // our browser session is cookie-based and needs CSRF protection.
            http.csrf(csrf -> csrf.disable());
        }
        if (settings.isCspEnabled()) {
            // SOLUTION: external application JS/CSS remain usable; inline handlers
            // and inline <style> payloads are blocked. Do not add 'unsafe-inline'.
            http.headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self'; " +
                            "connect-src 'self'; object-src 'none'; base-uri 'self'; frame-ancestors 'self'; form-action 'self'")));
        }
        // With csp-enabled=false no CSP is installed on the demo pages. This is
        // necessary for the instructor's CSS injection and the inline XSS handler.
        return http.build();
    }
}
