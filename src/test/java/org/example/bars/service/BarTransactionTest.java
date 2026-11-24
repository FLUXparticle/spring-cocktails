package org.example.bars.service;

import org.example.bars.entry.Bar;
import org.example.bars.repository.BarHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BarTransactionTest {
    @Autowired BarService service;
    @Autowired BarHistoryRepository history;

    @Test void failedUpdateRollsBackHistory() {
        Bar bar = service.create(new Bar("Testbar", "test@example.com", "Alt"));
        Bar stale = service.get(bar.getId());
        Bar current = service.get(bar.getId());
        current.setAddress("Zwischenstand");
        service.update(current);
        long historyBefore = history.count();

        stale.setAddress("Veraltete Änderung");
        assertThrows(OptimisticLockingFailureException.class, () -> service.update(stale));

        assertEquals("Zwischenstand", service.get(bar.getId()).getAddress());
        assertEquals(historyBefore, history.count());
    }
}
