package org.example.cocktails.bars.service;

import org.example.cocktails.bars.entry.*;
import org.example.cocktails.bars.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
@Transactional(transactionManager = "barsTransactionManager", readOnly = true)
public class BarQueryService {
    private final BarRepository repository;

    public BarQueryService(BarRepository repository) {
        this.repository = repository;
    }

    public BarPage find(String search, String field, String direction) {
        if (!Set.of("id", "name", "email", "address").contains(field))
            throw new IllegalArgumentException("Unbekanntes Sortierfeld.");
        if (!Set.of("ASC", "DESC").contains(direction.toUpperCase(Locale.ROOT)))
            throw new IllegalArgumentException("Sortierrichtung muss ASC oder DESC sein.");
        String term = search == null ? "" : search.trim();
        Sort sort = Sort.by(Sort.Direction.fromString(direction), field);
        if (!field.equals("id")) sort = sort.and(Sort.by("id"));
        // TODO: Aufgabe - Paging: Mit PageRequest die Paging-Methoden des Repositories verwenden und Gesamtzahl sowie Seiteninhalt übernehmen.
        List<Bar> bars = term.isEmpty() ? repository.findAll(sort)
                : repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrAddressContainingIgnoreCase(term, term, term, sort);
        return new BarPage(bars.size(), bars);
    }

    public record BarPage(long total, List<Bar> bars) {
    }
}
