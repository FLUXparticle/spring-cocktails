package org.example.bars.service;

import org.example.bars.entry.*;
import org.example.bars.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class BarService {
    private final BarRepository bars;
    private final BarHistoryRepository history;

    public BarService(BarRepository bars, BarHistoryRepository history) {
        this.bars = bars;
        this.history = history;
    }

    public Bar get(Long id) {
        return bars.findById(id).orElseThrow(() -> new BarNotFoundException(id));
    }

    public List<BarHistory> history(Long id) {
        return history.findByBarIdOrderByOccurredAtDescIdDesc(id);
    }

    @Transactional
    public Bar create(Bar input) {
        Bar bar = bars.save(input);
        history.save(new BarHistory(bar.getId(), "CREATE", bar.snapshot()));
        return bar;
    }

    public Bar update(Bar input) {
        // TODO: Aufgabe - Transaktionen: Historie und Bar gemeinsam in einer Transaktion speichern.
        String before = get(input.getId()).snapshot();
        // Absichtlich zuerst: Ohne Transaktion bleibt dieser Eintrag bei einem Versionskonflikt stehen.
        history.save(new BarHistory(input.getId(), "UPDATE", before + " → " + input.snapshot()));
        return bars.save(input);
    }

    @Transactional
    public void delete(Long id) {
        Bar bar = get(id);
        history.save(new BarHistory(id, "DELETE", bar.snapshot()));
        bars.delete(bar);
    }

}
