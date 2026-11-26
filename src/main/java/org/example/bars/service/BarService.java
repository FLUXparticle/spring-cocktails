package org.example.bars.service;

import org.example.bars.entry.*;
import org.example.bars.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class BarService {
    private final BarRepository bars;
    private final BarHistoryRepository history;
    private final ApplicationEventPublisher events;

    public BarService(BarRepository bars, BarHistoryRepository history, ApplicationEventPublisher events) {
        this.bars = bars;
        this.history = history;
        this.events = events;
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

    @Transactional
    public Bar update(Bar input) {
        String before = get(input.getId()).snapshot();
        // Die Transaktion rollt diesen Eintrag zurück, wenn das Speichern der Bar scheitert.
        history.save(new BarHistory(input.getId(), "UPDATE", before + " → " + input.snapshot()));
        Bar saved = bars.save(input);
        events.publishEvent(new BarChanged(saved.getId(), saved.getEmail()));
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        Bar bar = get(id);
        history.save(new BarHistory(id, "DELETE", bar.snapshot()));
        bars.delete(bar);
    }

}
