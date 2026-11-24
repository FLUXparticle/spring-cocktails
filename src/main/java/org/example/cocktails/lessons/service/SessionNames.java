package org.example.cocktails.lessons.service;

import org.springframework.stereotype.*;
import org.springframework.web.context.annotation.*;

import java.util.*;

@Component
@SessionScope
public class SessionNames {
    private final List<String> names = new ArrayList<>();

    public synchronized void add(String name) {
        names.add(name);
    }

    public synchronized List<String> all() {
        return List.copyOf(names);
    }
}
