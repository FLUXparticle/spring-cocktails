package org.example.bars.repository;

import org.example.bars.entry.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BarHistoryRepository extends JpaRepository<BarHistory, Long> {
    List<BarHistory> findByBarIdOrderByOccurredAtDescIdDesc(Long barId);
}
