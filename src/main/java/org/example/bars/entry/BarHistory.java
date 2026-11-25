package org.example.bars.entry;

import jakarta.persistence.*;

import java.time.*;

@Entity
public class BarHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Deliberately no foreign key: the audit trail survives deletion of a bar.
    private Long barId;
    private Instant occurredAt;
    private String action;
    @Column(length = 2000)
    private String details;

    protected BarHistory() {
    }

    public BarHistory(Long barId, String action, String details) {
        this.barId = barId;
        this.action = action;
        this.details = details;
        occurredAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getBarId() {
        return barId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }
}
