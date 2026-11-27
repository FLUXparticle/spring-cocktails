package org.example.security.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    // Hilfsendpunkt für die Cookie-basierte Anmeldung im Security Lab.
    @GetMapping("/security-demo/session")
    public SessionView session(Authentication auth, CsrfToken csrf) {
        if (csrf != null) csrf.getToken(); // Im unsicheren Lab-Modus ist CSRF ausgeschaltet.
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        return new SessionView(authenticated, authenticated ? auth.getName() : null);
    }

    public record SessionView(boolean authenticated, String username) {
    }
}
