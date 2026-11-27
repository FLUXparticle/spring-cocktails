package org.example.security.config;

import org.springframework.boot.context.properties.*;

import java.net.*;

@ConfigurationProperties("demo.security")
public class DemoSettings {
    private boolean csrfEnabled = true;
    private boolean cspEnabled = true;
    private boolean escapeNotes = true;
    private boolean allowAnyOrigin;
    // Adresse für die Demo-Links, unabhängig von der CORS-Freigabe.
    private String attackerOrigin = "http://localhost:1337";

    public boolean isCsrfEnabled() {
        return csrfEnabled;
    }

    public void setCsrfEnabled(boolean value) {
        csrfEnabled = value;
    }

    public boolean isCspEnabled() {
        return cspEnabled;
    }

    public void setCspEnabled(boolean value) {
        cspEnabled = value;
    }

    public boolean isEscapeNotes() {
        return escapeNotes;
    }

    public void setEscapeNotes(boolean value) {
        escapeNotes = value;
    }

    public boolean isAllowAnyOrigin() {
        return allowAnyOrigin;
    }

    public void setAllowAnyOrigin(boolean value) {
        allowAnyOrigin = value;
    }

    public String getAttackerOrigin() {
        return attackerOrigin;
    }

    public void setAttackerOrigin(String value) {
        URI origin = URI.create(value);
        if (!"http".equals(origin.getScheme()) || !("localhost".equals(origin.getHost()) || "127.0.0.1".equals(origin.getHost()))
                || origin.getPort() < 1 || origin.getPort() > 65535 || origin.getRawUserInfo() != null
                || (origin.getRawPath() != null && !origin.getRawPath().isEmpty()) || origin.getRawQuery() != null || origin.getFragment() != null)
            throw new IllegalArgumentException("Der Demo-Angreifer muss eine lokale HTTP-Origin mit Port sein.");
        attackerOrigin = value;
    }
}
