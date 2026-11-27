package org.example.security.controller;

import org.example.security.config.DemoSettings;
import org.example.security.service.DemoNoteService;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import java.util.*;

@RestController
@RequestMapping("/security-demo")
public class DemoNoteController {
    private final DemoNoteService notes;
    private final DemoSettings settings;

    public DemoNoteController(DemoNoteService notes, DemoSettings settings) {
        this.notes = notes;
        this.settings = settings;
    }

    @GetMapping("/config")
    public Mode mode() {
        return new Mode(settings.isCsrfEnabled(), settings.isCspEnabled(), settings.isEscapeNotes(), settings.isAllowAnyOrigin(), settings.getAttackerOrigin());
    }

    @GetMapping("/notes/me")
    public Note mine(Authentication auth) {
        return new Note(auth.getName(), notes.get(auth.getName()));
    }

    @PostMapping(value = "/notes/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Note saveJson(Authentication auth, @Valid @RequestBody NoteInput input) {
        return save(auth, input.text());
    }

    @PostMapping(value = "/notes/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Note saveForm(Authentication auth, @RequestParam String text) {
        if (text.length() > 16000)
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST);
        return save(auth, text);
    }

    @GetMapping("/notes/all")
    public Map<String, String> all() {
        Map<String, String> result = new TreeMap<>();
        notes.all().forEach((username, text) -> result.put(forHtml(username), forHtml(text)));
        return result;
    }

    private Note save(Authentication auth, String text) {
        notes.save(auth.getName(), text);
        return new Note(auth.getName(), text);
    }

    private String forHtml(String text) {
        // SOLUTION requested for the exercise: HtmlUtils.htmlEscape(text).
        // Escape at the HTML output boundary, not before storage or for every
        // JSON field. The personal editor uses textarea.value and needs raw text.
        // Alternatively replace admin innerHTML with DOM nodes + textContent.
        return settings.isEscapeNotes() ? HtmlUtils.htmlEscape(text) : text;
    }

    public record NoteInput(@NotNull @Size(max = 16000) String text) {
    }

    public record Note(String username, String text) {
    }

    public record Mode(boolean csrfEnabled, boolean cspEnabled, boolean escapeNotes, boolean allowAnyOrigin,
                       String attackerOrigin) {
    }
}
