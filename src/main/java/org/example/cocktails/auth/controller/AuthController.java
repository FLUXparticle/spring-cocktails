package org.example.cocktails.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.cocktails.auth.service.TokenService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenService tokens;

    public AuthController(TokenService tokens) {
        this.tokens = tokens;
    }

    @PostMapping("/login")
    @Operation(tags = "JWT-Anmeldung (vorgegeben)", summary = "JWT anfordern",
            description = "Zugangsdaten prüfen. Die Antwort enthält den Token; im Swagger-Dialog Authorize eintragen. Kein Cookie und keine Session.")
    @ApiResponse(responseCode = "200", description = "HS256-Token, eine Stunde gültig.")
    @ApiResponse(responseCode = "400", description = "Zugangsdaten fehlen.")
    @ApiResponse(responseCode = "401", description = "Benutzername oder Passwort falsch.")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = tokens.generateToken(request.username(), request.password());
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(new TokenResponse(token));
    }

    @GetMapping("/me")
    @Operation(tags = "JWT-Anmeldung (vorgegeben)", summary = "WhoAmI: aktuellen Benutzer anzeigen")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Benutzer und Berechtigungen aus Spring Security.")
    @ApiResponse(responseCode = "401", description = "Token fehlt, ist ungültig oder abgelaufen.")
    public UserView whoAmI(Authentication user) {
        return new UserView(user.getName(), user.getAuthorities().stream()
                .map(authority -> authority.getAuthority()).toList());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void invalidCredentials() {
    }

    public record LoginRequest(@NotBlank @Schema(example = "test") String username,
                               @NotBlank @Schema(example = "test", format = "password") String password) {
    }

    public record TokenResponse(@Schema(description = "JWT für den Authorization-Header; durch Login erzeugen.") String token) {
    }

    public record UserView(@Schema(example = "test") String username,
                           @Schema(example = "[\"FACTOR_BEARER\",\"SCOPE_USER\"]") List<String> authorities) {
    }
}
