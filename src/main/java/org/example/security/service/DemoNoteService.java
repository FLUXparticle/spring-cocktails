package org.example.security.service;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.concurrent.*;

@Service
public class DemoNoteService {
    private final Map<String, String> notes = new ConcurrentHashMap<>();

    public String get(String username) {
        return notes.getOrDefault(username, "");
    }

    public void save(String username, String text) {
        notes.put(username, text);
    }

    public Map<String, String> all() {
        return new TreeMap<>(notes);
    }
}
