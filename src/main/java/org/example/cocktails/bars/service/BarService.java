package org.example.cocktails.bars.service;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.example.cocktails.bars.entry.*;
import org.example.cocktails.bars.repository.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.validation.annotation.*;

import java.util.*;

@Service
@Validated
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

    public Map<Long, Long> versions() {
        Map<Long, Long> versions = new LinkedHashMap<>();
        for (Bar bar : bars.findAll()) versions.put(bar.getId(), BarVersions.get(bar));
        return versions;
    }

    @Transactional("barsTransactionManager")
    public Bar create(@Valid Bar input) {
        Bar bar = bars.saveAndFlush(new Bar(input.getName(), input.getEmail(), input.getAddress()));
        history.save(new BarHistory(bar.getId(), "CREATE", bar.snapshot()));
        return bar;
    }

    @Transactional("barsTransactionManager")
    public Bar update(@Valid Bar input) {
        Bar current = get(input.getId());
        checkVersion(current, BarVersions.get(input));
        String before = current.snapshot();
        current.setName(input.getName());
        current.setEmail(input.getEmail());
        current.setAddress(input.getAddress());
        bars.flush(); // Detect a race before recording history or returning to MVC.
        history.save(new BarHistory(current.getId(), "UPDATE", before + " → " + current.snapshot()));
        events.publishEvent(new BarChanged(current.getId(), current.getEmail()));
        return current;
    }

    @Transactional("barsTransactionManager")
    public void delete(Long id, Long version) {
        Bar current = get(id);
        checkVersion(current, version);
        history.save(new BarHistory(id, "DELETE", current.snapshot()));
        bars.delete(current);
        bars.flush();
    }

    public void bulkUpdate(Map<Long, Long> versions, @Size(max = 20) String address) {
        // TODO: Aufgabe - Transaktionen: Alle Änderungen und Historieneinträge dieses Batches in einer Transaktion speichern.
        var all = bars.findAll();
        if (all.size() != versions.size()) throw new BarConflictException();
        for (Bar current : all) {
            checkVersion(current, versions.get(current.getId()));
            String before = current.snapshot();
            current.setAddress(address);
            bars.saveAndFlush(current);
            history.save(new BarHistory(current.getId(), "UPDATE", before + " → " + current.snapshot()));
            events.publishEvent(new BarChanged(current.getId(), current.getEmail()));
        }
        bars.flush();
    }

    private void checkVersion(Bar current, Long version) {
        Long currentVersion = BarVersions.get(current);
        if (currentVersion != null && !Objects.equals(currentVersion, version))
            throw new BarConflictException(current.getId());
    }
}
