package org.example.cocktails.auth;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JwtDecoder decoder;
    @Autowired JwtEncoder encoder;

    private String login(String username, String password) throws Exception {
        var result = mvc.perform(post("/api/auth/login").contentType("application/json")
                        .content(json.writeValueAsString(java.util.Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andExpect(cookie().doesNotExist("jwt"))
                .andExpect(cookie().doesNotExist("JSESSIONID"))
                .andReturn();
        return json.readTree(result.getResponse().getContentAsString()).path("token").asString();
    }

    @Test void loginIssuesOneHourHs256Token() throws Exception {
        Jwt token = decoder.decode(login("test", "test"));
        assertEquals("test", token.getSubject());
        assertEquals("HS256", token.getHeaders().get("alg"));
        assertEquals(java.util.List.of("USER"), token.getClaimAsStringList("scope"));
        assertEquals(Duration.ofHours(1), Duration.between(token.getIssuedAt(), token.getExpiresAt()));
    }

    @Test void invalidCredentialsReturn401AndMissingFieldsReturn400() throws Exception {
        mvc.perform(post("/api/auth/login").contentType("application/json")
                        .content("{\"username\":\"test\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test void bearerTokenIdentifiesCurrentUser() throws Exception {
        String token = login("test", "test");
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.authorities").value(hasItem("SCOPE_USER")));
    }

    @Test void missingMalformedExpiredAndTamperedTokensReturn401() throws Exception {
        mvc.perform(get("/api/fridge/ingredients")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/fridge/ingredients").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
        Instant old = Instant.now().minusSeconds(7200);
        String expired = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
                JwtClaimsSet.builder().subject("test").claim("scope", java.util.List.of("USER"))
                        .issuedAt(old).expiresAt(old.plusSeconds(3600)).build())).getTokenValue();
        mvc.perform(get("/api/fridge/ingredients").header("Authorization", "Bearer " + expired))
                .andExpect(status().isUnauthorized());
        String[] parts = login("test", "test").split("\\.");
        String original = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String changed = original.replace("\"test\"", "\"demo\"");
        assertNotEquals(original, changed);
        parts[1] = Base64.getUrlEncoder().withoutPadding().encodeToString(changed.getBytes(StandardCharsets.UTF_8));
        mvc.perform(get("/api/fridge/ingredients").header("Authorization", "Bearer " + String.join(".", parts)))
                .andExpect(status().isUnauthorized());
    }

    @Test void userRoleIsRequiredForFridge() throws Exception {
        String withoutUserRole = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
                JwtClaimsSet.builder().subject("test").claim("scope", java.util.List.of("OTHER"))
                        .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600)).build())).getTokenValue();
        mvc.perform(get("/api/fridge/ingredients").header("Authorization", "Bearer " + withoutUserRole))
                .andExpect(status().isForbidden());
    }

    @Test void mvcSessionStillUsesCsrfButDoesNotAuthenticateApi() throws Exception {
        var result = mvc.perform(post("/login").with(csrf()).param("username", "test").param("password", "test"))
                .andExpect(status().is3xxRedirection()).andReturn();
        var session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);
        mvc.perform(post("/mvc/user/fridge").session(session).param("id", "29").param("action", "add"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/fridge/ingredients").session(session)).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/fridge/ingredients").cookie(new Cookie("jwt", login("test", "test"))))
                .andExpect(status().isUnauthorized());
    }

    @Test void swaggerIsPublicAndRestCartIsAbsent() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
        mvc.perform(get("/api/cart").header("Authorization", "Bearer " + login("test", "test")))
                .andExpect(status().isNotFound());
    }
}
