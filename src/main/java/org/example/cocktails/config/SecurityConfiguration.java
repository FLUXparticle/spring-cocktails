package org.example.cocktails.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/mvc/user/**", "/api/fridge/**").hasRole("USER")
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.spa())
                .exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        request -> request.getRequestURI().startsWith(request.getContextPath() + "/api/")))
                .formLogin(login -> login.defaultSuccessUrl("/mvc"))
                .logout(logout -> logout.logoutSuccessUrl("/mvc"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername("test").password(encoder.encode("test")).roles("USER").build(),
                User.withUsername("demo").password(encoder.encode("demo")).roles("USER").build());
    }
}
